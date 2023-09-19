package csci318.demo.cargotracker.bookingms.application.internal.commandservices;

import csci318.demo.cargotracker.bookingms.application.internal.outboundservices.acl.ExternalCargoRoutingService;
import csci318.demo.cargotracker.bookingms.domain.model.aggregates.BookingId;
import csci318.demo.cargotracker.bookingms.domain.model.aggregates.Cargo;
import csci318.demo.cargotracker.bookingms.domain.model.commands.BookCargoCommand;
import csci318.demo.cargotracker.bookingms.domain.model.commands.RouteCargoCommand;
import csci318.demo.cargotracker.bookingms.domain.model.entities.Location;
import csci318.demo.cargotracker.bookingms.domain.model.valueobjects.CargoItinerary;
import csci318.demo.cargotracker.bookingms.domain.model.valueobjects.RouteSpecification;
import csci318.demo.cargotracker.bookingms.infrastructure.repositories.CargoRepository;
import org.springframework.stereotype.Service;


import java.util.UUID;

/**
 * Application Service class for the Cargo Booking Commands
 */

@Service
public class CargoBookingCommandService {


    private CargoRepository cargoRepository;
    private ExternalCargoRoutingService externalCargoRoutingService;

    public CargoBookingCommandService(CargoRepository cargoRepository){

        this.cargoRepository = cargoRepository;
        this.externalCargoRoutingService = externalCargoRoutingService;
    }

    /**
     * Service Command method to book a new Cargo
     * @return BookingId of the Cargo
     */

    public BookingId bookCargo(BookCargoCommand bookCargoCommand){

        String random = UUID.randomUUID().toString().toUpperCase();
        String bookIdStr = random.substring(0, random.indexOf("-"));
        System.out.println("Random is :"+bookIdStr);
        bookCargoCommand.setBookingId(bookIdStr);
        Cargo cargo = new Cargo(bookCargoCommand);
        cargoRepository.save(cargo);
        return new BookingId(bookIdStr);
    }

    /**
     * Service Command method to assign a route to a Cargo
     * @param routeCargoCommand
     */

    public void assignRouteToCargo(RouteCargoCommand routeCargoCommand){

        Cargo cargo = cargoRepository.findByBookingId(new BookingId(routeCargoCommand.getCargoBookingId()));
        CargoItinerary cargoItinerary = externalCargoRoutingService.fetchRouteForSpecification(new RouteSpecification(
                new Location(routeCargoCommand.getOriginLocation()),
                new Location(routeCargoCommand.getDestinationLocation()),
                routeCargoCommand.getArrivalDeadline()
        ));
        routeCargoCommand.setCargoItinerary(cargoItinerary);
        cargo.assignToRoute(routeCargoCommand);
        cargoRepository.save(cargo);

    }


}
