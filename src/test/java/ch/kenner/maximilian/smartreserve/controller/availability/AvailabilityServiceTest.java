package ch.kenner.maximilian.smartreserve.controller.availability;

import ch.kenner.maximilian.smartreserve.base.TimeFrame;
import ch.kenner.maximilian.smartreserve.controller.reservation.GuestReservationService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * LB1 – Auftrag 1, Kategorie «Sammlungen &amp; Listen».
 *
 * <p>Testet {@link AvailabilityService#getAvailableTimeFrames(LocalDate)}:
 * das Erzeugen freier Zeitfenster aus einer (ggf. unsortierten) Liste von
 * Reservationen. Deckt leere Listen, Sortierung sowie Lücken zwischen,
 * vor und nach den Reservationen ab.
 */
class AvailabilityServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GuestReservationService guestReservationService;

    private AvailabilityService availabilityService;

    private static final LocalDate DATE = LocalDate.of(2026, 8, 24);
    private static final ZoneId ZONE = ZoneId.of("Europe/Zurich");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        availabilityService = new AvailabilityService(guestReservationService, reservationRepository);
        // @Value-Feld per Reflection setzen (reiner Unit-Test ohne Spring-Kontext)
        ReflectionTestUtils.setField(availabilityService, "timezone", ZONE);
    }

    private Reservation reservation(int startHour, int endHour) {
        Reservation r = new Reservation();
        r.setStartTime(ZonedDateTime.of(DATE, LocalTime.of(startHour, 0), ZONE));
        r.setEndTime(ZonedDateTime.of(DATE, LocalTime.of(endHour, 0), ZONE));
        return r;
    }

    @Test
    void emptyDayShouldReturnSingleFullDayFrame() {
        when(reservationRepository.getReservationsByStartTimeIsAfterAndStartTimeBefore(any(), any()))
                .thenReturn(new ArrayList<>());

        List<TimeFrame> frames = availabilityService.getAvailableTimeFrames(DATE);

        assertEquals(1, frames.size());
        assertEquals(DATE.atStartOfDay(), frames.get(0).startTime);
        assertEquals(DATE.plusDays(1).atStartOfDay(), frames.get(0).endTime);
    }

    @Test
    void singleMiddayReservationShouldSplitDayInTwo() {
        when(reservationRepository.getReservationsByStartTimeIsAfterAndStartTimeBefore(any(), any()))
                .thenReturn(new ArrayList<>(List.of(reservation(10, 11))));

        List<TimeFrame> frames = availabilityService.getAvailableTimeFrames(DATE);

        assertEquals(2, frames.size());
        assertEquals(DATE.atStartOfDay(), frames.get(0).startTime);
        assertEquals(DATE.atTime(10, 0), frames.get(0).endTime);
        assertEquals(DATE.atTime(11, 0), frames.get(1).startTime);
        assertEquals(DATE.plusDays(1).atStartOfDay(), frames.get(1).endTime);
    }

    @Test
    void multipleReservationsShouldProduceGapsInBetween() {
        when(reservationRepository.getReservationsByStartTimeIsAfterAndStartTimeBefore(any(), any()))
                .thenReturn(new ArrayList<>(List.of(
                        reservation(9, 10),
                        reservation(13, 14),
                        reservation(17, 18))));

        List<TimeFrame> frames = availabilityService.getAvailableTimeFrames(DATE);

        assertEquals(4, frames.size());
        assertEquals(DATE.atStartOfDay(), frames.get(0).startTime);
        assertEquals(DATE.atTime(9, 0), frames.get(0).endTime);

        assertEquals(DATE.atTime(10, 0), frames.get(1).startTime);
        assertEquals(DATE.atTime(13, 0), frames.get(1).endTime);

        assertEquals(DATE.atTime(14, 0), frames.get(2).startTime);
        assertEquals(DATE.atTime(17, 0), frames.get(2).endTime);

        assertEquals(DATE.atTime(18, 0), frames.get(3).startTime);
        assertEquals(DATE.plusDays(1).atStartOfDay(), frames.get(3).endTime);
    }

    @Test
    void unsortedReservationsShouldBeSortedBeforeGapCalculation() {
        when(reservationRepository.getReservationsByStartTimeIsAfterAndStartTimeBefore(any(), any()))
                .thenReturn(new ArrayList<>(List.of(
                        reservation(15, 16),
                        reservation(8, 9))));

        List<TimeFrame> frames = availabilityService.getAvailableTimeFrames(DATE);

        assertEquals(3, frames.size());
        assertEquals(DATE.atTime(8, 0), frames.get(0).endTime);
        assertEquals(DATE.atTime(9, 0), frames.get(1).startTime);
        assertEquals(DATE.atTime(15, 0), frames.get(1).endTime);
        assertEquals(DATE.atTime(16, 0), frames.get(2).startTime);
    }
}
