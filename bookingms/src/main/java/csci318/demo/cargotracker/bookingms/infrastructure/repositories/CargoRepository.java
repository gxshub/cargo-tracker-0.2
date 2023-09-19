package csci318.demo.cargotracker.bookingms.infrastructure.repositories;

import csci318.demo.cargotracker.bookingms.domain.model.aggregates.BookingId;
import csci318.demo.cargotracker.bookingms.domain.model.aggregates.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository class for the Cargo Aggregate
 */
public interface CargoRepository extends JpaRepository<Cargo, Long> {

     Cargo findByBookingId(BookingId BookingId);

     List<BookingId> findAllBookingIds();

     List<Cargo> findAll();

}
