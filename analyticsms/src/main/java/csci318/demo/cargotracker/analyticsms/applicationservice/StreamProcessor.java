package csci318.demo.cargotracker.analyticsms.applicationservice;

import csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class StreamProcessor {
    public final static String BOOKING_STATE_STORE = "cargo-booking";

    @Bean
    public Consumer<KStream<String, CargoBookedEvent>> process() {
        return inputStream -> {

            //generate RUNNING total booking amounts by destination
            KStream<String, Long> aggregatedStream = inputStream.map((key, value) -> {
                        String destCity = value.getCargoBookedEventData().getDestLocation();
                        Long bookAmount = value.getCargoBookedEventData().getBookingAmount().longValue();
                        return KeyValue.pair(destCity, bookAmount);
                    }).
                    groupByKey(Grouped.with(Serdes.String(), Serdes.Long())).
                    reduce(Long::sum).toStream();

            //just print the stream out to console
            aggregatedStream.
                    print(Printed.<String, Long>toSysOut().withLabel("Total booking amount by destination"));
        };
    }
}
