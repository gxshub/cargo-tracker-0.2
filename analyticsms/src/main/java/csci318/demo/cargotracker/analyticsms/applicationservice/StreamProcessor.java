package csci318.demo.cargotracker.analyticsms.applicationservice;

import csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class StreamProcessor {
    public final static String TOTAL_BOOKINGS = "total-bookings";

    @Bean
    public Consumer<KStream<String, CargoBookedEvent>> process() {
        return inputStream -> {

            //generate RUNNING total booking amounts by destination
            KTable<String, Long> totalBookings = inputStream.map((key, value) -> {
                        String destCity = value.getCargoBookedEventData().getDestLocation();
                        Long bookAmount = value.getCargoBookedEventData().getBookingAmount().longValue();
                        return KeyValue.pair(destCity, bookAmount);
                    }).
                    groupByKey(Grouped.with(Serdes.String(), Serdes.Long())).
                    reduce(Long::sum,
                            Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(TOTAL_BOOKINGS).
                                    withKeySerde(Serdes.String()).withValueSerde(Serdes.Long()));

            // print data to console (not part of the stream processing logic)
            totalBookings.toStream().
                    print(Printed.<String, Long>toSysOut().withLabel("Total bookings by city"));
        };
    }
}
