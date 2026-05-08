package ch.kenner.maximilian.smartreserve.model.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> getReservationsByUser_IdOrderByStartTimeAsc(String userId);
    List<Reservation> findByOrderByStartTimeAsc();
    List<Reservation> getReservationsByStatusOrderByStartTimeAsc(String status);

    boolean existsReservationsByStartTimeIsAfterAndStartTimeBefore(ZonedDateTime startTimeAfter, ZonedDateTime startTimeBefore);
}
