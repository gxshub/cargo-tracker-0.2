package csci318.demo.cargotracker.shareddomain.events;

/**
 * Event Class for the Cargo Routed Event. Wraps up the Cargo Routed Event Data
 */
public class CargoRoutedEvent {

    CargoRoutedEventData cargoRoutedEventData;
    public CargoRoutedEvent(CargoRoutedEventData cargoRoutedEventData){
        this.cargoRoutedEventData = cargoRoutedEventData;
    }

    public CargoRoutedEventData getCargoRoutedEventData(){
        return cargoRoutedEventData;
    }
}
