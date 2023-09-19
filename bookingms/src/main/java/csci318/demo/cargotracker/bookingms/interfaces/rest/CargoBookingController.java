package csci318.demo.cargotracker.bookingms.interfaces.rest;

import csci318.demo.cargotracker.bookingms.application.internal.commandservices.CargoBookingCommandService;
import csci318.demo.cargotracker.bookingms.application.internal.queryservices.CargoBookingQueryService;
import csci318.demo.cargotracker.bookingms.domain.model.aggregates.BookingId;
import csci318.demo.cargotracker.bookingms.domain.model.aggregates.Cargo;
import csci318.demo.cargotracker.bookingms.interfaces.rest.dto.BookCargoResource;
import csci318.demo.cargotracker.bookingms.interfaces.rest.transform.BookCargoCommandDTOAssembler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping("/cargobooking")
public class CargoBookingController {


    private CargoBookingCommandService cargoBookingCommandService; // Application Service Dependency

    private CargoBookingQueryService cargoBookingQueryService;

    /**
     * Provide the dependencies
     * @param cargoBookingCommandService
     */
    public CargoBookingController(CargoBookingCommandService cargoBookingCommandService, CargoBookingQueryService cargoBookingQueryService){
        this.cargoBookingCommandService = cargoBookingCommandService;
        this.cargoBookingQueryService = cargoBookingQueryService;
    }

    /**
     * POST method to book a cargo
     * @param bookCargoResource
     */

    @PostMapping
    @ResponseBody
    public BookingId bookCargo(@RequestBody  BookCargoResource bookCargoResource){
        System.out.println("****Cargo Booked ****"+bookCargoResource.getBookingAmount());
        BookingId bookingId  = cargoBookingCommandService.bookCargo(
                BookCargoCommandDTOAssembler.toCommandFromDTO(bookCargoResource));

        return bookingId;
    }

    /**
     * GET method to retrieve a Cargo
     * @param bookingId
     * @return Cargo
     */
    @GetMapping("/findCargo")
    @ResponseBody
    public Cargo findByBookingId(@RequestParam("bookingId") String bookingId){
        System.out.println("****Cargo BookingID ****"+bookingId);
        return cargoBookingQueryService.find(new BookingId(bookingId));
    }

    /**
     * GET method to retrieve a Cargo
     * @param
     * @return List<BookingId>
     */
    @GetMapping("/findAllBookingIds")
    @ResponseBody
    public List<BookingId> findAllBookingIds(){
        final List<BookingId> bookingIdList = cargoBookingQueryService.findAllBookingIds();
        System.out.println("****Cargo BookingID ****");
        bookingIdList.forEach(x->System.out.println(x.getBookingId()));
        return bookingIdList;
    }
}
