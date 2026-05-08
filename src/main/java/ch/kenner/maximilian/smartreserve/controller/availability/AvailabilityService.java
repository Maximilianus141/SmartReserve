package ch.kenner.maximilian.smartreserve.controller.availability;

import ch.kenner.maximilian.smartreserve.base.TimeFrame;
import ch.kenner.maximilian.smartreserve.controller.reservation.GuestReservationService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AvailabilityService {
    private final GuestReservationService guestReservationService;
    private final ReservationRepository reservationRepository;

    @Value("${spring.application.timezone}")
    private ZoneId timezone;


    public List<TimeFrame> getAvailableTimeFrames(LocalDate date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, LocalTime.from(date.atStartOfDay()), timezone);
        List<Reservation> res = reservationRepository.getReservationsByStartTimeIsAfterAndStartTimeBefore(
                date.atStartOfDay().atZone(timezone),
                date.plusDays(1).atStartOfDay().atZone(timezone));
        res.sort(Comparator.comparing(Reservation::getStartTime));

        Reservation temp = new Reservation();
        temp.setStartTime(date.atStartOfDay().plusDays(1).atZone(timezone));

        res.add(temp);
        List<TimeFrame> timeFrames = new ArrayList<>();
        Reservation lastReservation = null;
        for (Reservation reservation : res) {

            TimeFrame timeFrame = new TimeFrame();

            if (timeFrames.isEmpty())
                timeFrame.startTime = date.atStartOfDay();
            else
                timeFrame.startTime = lastReservation.getEndTime().toLocalDateTime();

            timeFrame.endTime = reservation.getStartTime().toLocalDateTime();
            lastReservation = reservation;
            timeFrames.add(timeFrame);
        }
        return timeFrames;
    }


}
