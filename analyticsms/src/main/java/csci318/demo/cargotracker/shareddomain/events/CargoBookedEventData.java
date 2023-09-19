package csci318.demo.cargotracker.shareddomain.events;

/**
 * Event Data for the Cargo Booked Event
 */
public class CargoBookedEventData {

    private String bookingId;
    private Integer bookingAmount;

    public CargoBookedEventData(){}
    public CargoBookedEventData(String bookingId){
        this.bookingId = bookingId;

    }
    public CargoBookedEventData(String bookingId, Integer bookingAmount){
        this.bookingId = bookingId;
        this.bookingAmount = bookingAmount;

    }

    public void setBookingId(String bookingId){this.bookingId = bookingId;}
    public String getBookingId(){return this.bookingId;}
    public Integer getBookingAmount() {
        return bookingAmount;
    }
    public void setBookingAmount(Integer bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    @Override
    public String toString() {
        return "CargoBookedEventData{" +
                "bookingId='" + bookingId + '\'' +
                ", bookingAmount=" + bookingAmount +
                '}';
    }
}
