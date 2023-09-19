package csci318.demo.cargotracker.analyticsms.applicationservice;

import csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.streams.kstream.KStream;

import java.util.function.Consumer;

@Configuration
public class StreamProcessor {
    public final static String BOOKING_STATE_STORE = "cargo-booking";

    @Bean
    public Consumer<KStream<String, CargoBookedEvent>> process(){
        return inputStream -> {
            KStream<String, Long> transformedStream = inputStream.map((x,y)-> KeyValue.pair("cargo booking",
                            y.getCargoBookedEventData().getBookingAmount().longValue()));

            //get "group by sum"
           transformedStream.
                   groupByKey(Grouped.with(Serdes.String(),Serdes.Long())).
                    reduce(Long::sum).toStream().
                   print(Printed.<String, Long>toSysOut().withLabel("GroupBy Sum"));

            //get "group by count"
            //transformedStream.
            //        groupBy((k,v)-> k).count().toStream();
                            //.
                    //print(Printed.<String, Long>toSysOut().withLabel("GroupBy Count"));
        };
    }
}
