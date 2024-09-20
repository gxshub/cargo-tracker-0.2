package csci318.demo.cargotracker.client.dto;



import java.io.Serializable;

/**
 * Aggregate Identifier for the Cargo Aggregate
 */
public class BookingIdDto implements Serializable {

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
