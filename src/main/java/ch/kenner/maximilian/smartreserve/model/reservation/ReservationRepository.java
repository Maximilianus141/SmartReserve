package ch.kenner.maximilian.smartreserve.model.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> getReservationsByUser_Id(String userId);

    List<Reservation> getReservationsByStatus(String status);
    boolean existsReservationsByStartTimeIsAfterAndStartTimeBefore(ZonedDateTime startTimeAfter, ZonedDateTime startTimeBefore);
}
