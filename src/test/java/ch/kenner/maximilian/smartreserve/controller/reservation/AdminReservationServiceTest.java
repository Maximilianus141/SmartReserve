package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.controller.reservation.dto.AdminReservationRequestDTO;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.service.ServiceRepository;
import ch.kenner.maximilian.smartreserve.model.user.User;
import ch.kenner.maximilian.smartreserve.model.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * LB1 – Auftrag 1, Kategorie «Fehlerbehandlung &amp; Edge Cases».
 *
 * <p>Testet das gezielte Abfangen von unzulässigen Werten: unbekannter User,
 * unbekannter Service und unbekannte Reservation führen zu einer
 * {@link EntityNotFoundException}.
 */
class AdminReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ServiceRepository serviceRepository;

    private AdminReservationService adminReservationService;

    private static final ZoneId ZONE = ZoneId.of("Europe/Zurich");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminReservationService = new AdminReservationService(
                reservationRepository, userRepository, serviceRepository);
    }

    private User user(String id) {
        return new User(id, "username", "email@example.com");
    }

    private Service service() {
        Service s = new Service();
        s.setId(1L);
        s.setName("Haircut");
        s.setDescription("description");
        s.setDurationSeconds(180L);
        return s;
    }

    private AdminReservationRequestDTO dto(String userId, Long serviceId) {
        ZonedDateTime start = ZonedDateTime.of(2026, 8, 24, 10, 0, 0, 0, ZONE);
        return new AdminReservationRequestDTO(userId, serviceId, "PENDING", start);
    }

    @Test
    void insertReservationWithUnknownUserShouldThrow() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adminReservationService.insertReservation(dto("unknown", 1L)));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void insertReservationWithUnknownServiceShouldThrow() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user("user-1")));
        when(serviceRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adminReservationService.insertReservation(dto("user-1", 99L)));
        assertEquals("Service not found", ex.getMessage());
    }

    @Test
    void insertReservationShouldSave() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user("user-1")));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service()));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = adminReservationService.insertReservation(dto("user-1", 1L));

        assertEquals("user-1", result.getUser().getId());
        assertEquals(1L, result.getService().getId());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void updateNonExistentReservationShouldThrow() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adminReservationService.updateReservation(dto("user-1", 1L), 99L));
        assertEquals("Reservation not found", ex.getMessage());
    }
}
