package csci318.demo.cargotracker.analyticsms.interfaces.rest;

import csci318.demo.cargotracker.analyticsms.applicationservice.InteractiveQuery;
import csci318.demo.cargotracker.analyticsms.interfaces.rest.dto.BookingsByCity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping("/queries")
public class QueryController {

    private final InteractiveQuery interactiveQuery;

    public QueryController(InteractiveQuery interactiveQuery) {
        this.interactiveQuery = interactiveQuery;
    }

    @GetMapping("/findAllBookingsByCity")
    @ResponseBody
    public List<BookingsByCity> findAllBookingsByCity() {
        return interactiveQuery.getAllBookingsByCity();
    }
}
