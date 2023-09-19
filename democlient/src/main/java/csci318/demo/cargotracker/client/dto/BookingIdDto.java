package csci318.demo.cargotracker.client.dto;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Aggregate Identifier for the Cargo Aggregate
 */
@Embeddable
public class BookingIdDto implements Serializable {

    @Column(name="booking_id")
    private String bookingId;

    public BookingIdDto(){}

    public BookingIdDto(String bookingId){this.bookingId = bookingId;}

    public String getBookingId(){return this.bookingId;}

    @Override
    public String toString() {
        return "BookingId{" +
                "bookingId='" + bookingId + '\'' +
                '}';
    }
}
