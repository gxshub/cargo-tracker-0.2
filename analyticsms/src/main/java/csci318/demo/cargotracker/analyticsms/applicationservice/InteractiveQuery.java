package csci318.demo.cargotracker.analyticsms.applicationservice;

import csci318.demo.cargotracker.analyticsms.interfaces.rest.dto.BookingsByCity;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InteractiveQuery {

    private final InteractiveQueryService interactiveQueryService;

    public InteractiveQuery(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    public List<BookingsByCity> getAllBookingsByCity() {
        List<BookingsByCity> allBookingsByCity = new ArrayList<>();
        KeyValueIterator<String, Long> all = getTotalBookingsKSStore().all();
        while (all.hasNext()) {
            KeyValue<String, Long> ks = all.next();
            BookingsByCity quantityPerCity = new BookingsByCity();
            quantityPerCity.setCity(ks.key);
            quantityPerCity.setBookingQuantity(ks.value);
            allBookingsByCity.add(quantityPerCity);
        }
        return allBookingsByCity;
    }

    private ReadOnlyKeyValueStore<String, Long> getTotalBookingsKSStore() {
        return this.interactiveQueryService.getQueryableStore(StreamProcessor.TOTAL_BOOKINGS,
                QueryableStoreTypes.keyValueStore());
    }
}
