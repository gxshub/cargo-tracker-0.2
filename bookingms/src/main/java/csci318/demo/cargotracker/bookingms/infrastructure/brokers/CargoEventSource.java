package csci318.demo.cargotracker.bookingms.infrastructure.brokers;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Interface depicting all output channels
 */
public interface CargoEventSource {

    @Output("cargoBookingChannel")
    MessageChannel cargoBooking();

    @Output("cargoRoutingChannel")
    MessageChannel cargoRouting();

}
