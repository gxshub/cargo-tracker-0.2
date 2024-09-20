package csci318.demo.cargotracker.bookingms.application.outboundservices;

import csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent;
import csci318.demo.cargotracker.shareddomain.events.CargoRoutedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 *
 */
@Service
public class CargoEventPublisherService {

    private final StreamBridge streamBridge;

    public CargoEventPublisherService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @TransactionalEventListener
    public void handleCargoBookedEvent(CargoBookedEvent cargoBookedEvent) {
        streamBridge.send("cargoBookingChannel", cargoBookedEvent);
    }

    @TransactionalEventListener
    public void handleCargoRoutedEvent(CargoRoutedEvent cargoRoutedEvent) {
        streamBridge.send("cargoRoutingChannel", cargoRoutedEvent);
    }
}