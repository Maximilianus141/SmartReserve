package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.base.ReservationConflict;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.GuestReservationRequestDTO;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.MyReservationResponseDTO;
import ch.kenner.maximilian.smartreserve.controller.service.ServiceService;
import ch.kenner.maximilian.smartreserve.controller.user.UserService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationStatus;
import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * LB1 – Auftrag 1, Kategorien «Kombinatorische Logik» und
 * «Fehlerbehandlung &amp; Edge Cases».
 *
 * <p>Kombinatorische Logik: Die Kollisionsprüfung
 * ({@code existsByStartTimeBeforeAndEndTimeAfter}) bildet die UND-Verknüpfung
 * «startet vor dem Ende UND endet nach dem Start» ab.
 *
 * <p>Fehlerbehandlung: null-/unzulässige Werte und Exceptions
 * (z.&nbsp;B. fremde Reservation, unbekannte ID, unbekannter Service).
 */
class GuestReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserService userService;
    @Mock
    private ServiceService serviceService;
    @Mock
    private Jwt jwt;

    private GuestReservationService guestReservationService;

    private static final ZoneId ZONE = ZoneId.of("Europe/Zurich");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        guestReservationService = new GuestReservationService(
                reservationRepository, userService, serviceService);
    }

    private User user(String id) {
        return new User(id, "username", "email@example.com");
    }

    private Service service(long durationSeconds, long breakSeconds) {
        Service s = new Service();
        s.setId(1L);
        s.setName("Haircut");
        s.setDescription("description");
        s.setDurationSeconds(durationSeconds);
        s.setAfterServiceBreakDurationSeconds(breakSeconds);
        return s;
    }

    private GuestReservationRequestDTO request(long serviceId, ZonedDateTime start) {
        GuestReservationRequestDTO dto = new GuestReservationRequestDTO();
        dto.setServiceId(serviceId);
        dto.setStartTime(start);
        return dto;
    }

    private ZonedDateTime start(int hour) {
        return ZonedDateTime.of(2026, 8, 24, hour, 0, 0, 0, ZONE);
    }

    // ---------------------------------------------------------------
    // Kombinatorische Logik: Kollisionsprüfung (UND-Verknüpfung)
    // ---------------------------------------------------------------

    @Test
    void postReservationWithoutConflictShouldSaveAsPending() throws ReservationConflict {
        when(serviceService.getServiceById(1L)).thenReturn(service(180L, 20L));
        when(reservationRepository.existsByStartTimeBeforeAndEndTimeAfter(any(), any()))
                .thenReturn(false);
        when(userService.getUser(jwt)).thenReturn(user("user-1"));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MyReservationResponseDTO result =
                guestReservationService.postMyReservation(jwt, request(1L, start(10)));

        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING.value, result.getStatus());
        assertEquals("Haircut", result.getService().getName());
        assertEquals(start(10), result.getStartTime());
    }

    @Test
    void postReservationWithConflictShouldThrow() {
        when(serviceService.getServiceById(1L)).thenReturn(service(180L, 20L));
        when(reservationRepository.existsByStartTimeBeforeAndEndTimeAfter(any(), any()))
                .thenReturn(true);

        assertThrows(ReservationConflict.class,
                () -> guestReservationService.postMyReservation(jwt, request(1L, start(10))));
    }

    // ---------------------------------------------------------------
    // Fehlerbehandlung & Edge Cases
    // ---------------------------------------------------------------

    @Test
    void cancelOwnReservationShouldSetCancelled() {
        User u = user("user-1");
        Reservation res = new Reservation();
        res.setId(42L);
        res.setUser(u);
        res.setStatus(ReservationStatus.CONFIRMED.value);

        when(userService.getUser(jwt)).thenReturn(u);
        when(reservationRepository.findById(42L)).thenReturn(Optional.of(res));

        MessageResponse response = guestReservationService.cancelMyReservation(jwt, 42L);

        assertEquals(ReservationStatus.CANCELLED.value, res.getStatus());
        assertTrue(response.getMessage().contains("42"));
    }

    @Test
    void cancelReservationOfAnotherUserShouldThrow() {
        User owner = user("owner-1");
        Reservation res = new Reservation();
        res.setId(42L);
        res.setUser(owner);

        when(userService.getUser(jwt)).thenReturn(user("requester-1"));
        when(reservationRepository.findById(42L)).thenReturn(Optional.of(res));

        IllegalAccessError ex = assertThrows(IllegalAccessError.class,
                () -> guestReservationService.cancelMyReservation(jwt, 42L));
        assertTrue(ex.getMessage().contains("does not belong"));
    }

    @Test
    void cancelNonExistentReservationShouldThrow() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> guestReservationService.cancelMyReservation(jwt, 99L));
    }

    @Test
    void postReservationWithUnknownServiceShouldThrow() {
        when(serviceService.getServiceById(999L)).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> guestReservationService.postMyReservation(jwt, request(999L, start(10))));
    }
}
