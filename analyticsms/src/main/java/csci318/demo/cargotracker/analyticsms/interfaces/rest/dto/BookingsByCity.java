package csci318.demo.cargotracker.analyticsms.interfaces.rest.dto;

public class BookingsByCity {

    private String city;
    private Long bookingQuantity;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getBookingQuantity() {
        return bookingQuantity;
    }

    public void setBookingQuantity(Long bookingQuantity) {
        this.bookingQuantity = bookingQuantity;
    }
}
