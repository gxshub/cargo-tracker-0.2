package csci318.demo.cargotracker.shareddomain.events;

/**
 * Event Data for the Cargo Booked Event
 */
public class CargoBookedEventData {

    private String bookingId;
    private Integer bookingAmount;
    private String destLocation;
    private String originLocation;

    public CargoBookedEventData(){}
    public CargoBookedEventData(String bookingId){
        this.bookingId = bookingId;

    }
    public CargoBookedEventData(String bookingId, Integer bookingAmount,
                                String originLocation, String destLocation){
        this.bookingId = bookingId;
        this.bookingAmount = bookingAmount;
        this.originLocation = originLocation;
        this.destLocation = destLocation;
    }

    public void setBookingId(String bookingId){this.bookingId = bookingId;}
    public String getBookingId(){return this.bookingId;}
    public Integer getBookingAmount() {
        return bookingAmount;
    }
    public void setBookingAmount(Integer bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public String getDestLocation() {
        return destLocation;
    }

    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    @Override
    public String toString() {
        return "CargoBookedEventData{" +
                "bookingId='" + bookingId + '\'' +
                ", bookingAmount=" + bookingAmount +
                ", destLocation='" + destLocation + '\'' +
                ", originLocation='" + originLocation + '\'' +
                '}';
    }
}
