package csci318.demo.cargotracker.trackingms.application;

import csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Event Handler for the Cargo Routed Event that the Tracking Bounded Context is interested in
 */
@Service
public class CargoRoutedEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CargoRoutedEventHandler.class);

    @Bean
    public Consumer<CargoBookedEvent> consume() {
        return payload -> log.info(payload.toString());
    }
}