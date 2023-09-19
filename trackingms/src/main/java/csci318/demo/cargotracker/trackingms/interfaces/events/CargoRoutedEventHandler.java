package csci318.demo.cargotracker.trackingms.interfaces.events;


import csci318.demo.cargotracker.shareddomain.events.CargoBookedEvent;
import csci318.demo.cargotracker.trackingms.infrastructure.brokers.CargoEventSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * Event Handler for the Cargo Routed Event that the Tracking Bounded Context is interested in
 */
@Service
@EnableBinding(CargoEventSource.class)
public class CargoRoutedEventHandler {

    @StreamListener(CargoEventSource.BOOKING_INPUT)
    public void receiveEvent(CargoBookedEvent cargoBookedEvent) {
        System.out.println("****READING FROM KAFKA TOPIC cargobookings: "+
                cargoBookedEvent.getCargoBookedEventData().getBookingId()+
                ", booking amount: " +
                cargoBookedEvent.getCargoBookedEventData().getBookingAmount()+"****");
    }
}
