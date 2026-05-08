package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.base.ReservationConflict;
import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.GuestReservationRequestDTO;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.MyReservationResponseDTO;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.ReservationResponseDTO;
import ch.kenner.maximilian.smartreserve.controller.service.ServiceService;
import ch.kenner.maximilian.smartreserve.controller.user.UserService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationStatus;
import ch.kenner.maximilian.smartreserve.model.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ServiceService serviceService;

    @Value("${spring.application.timezone}")
    private String timezone;


    public List<MyReservationResponseDTO> getMyReservations(Jwt jwt) {
        User user = getUser(jwt);

        List<Reservation> res = reservationRepository.getReservationsByUser_IdOrderByStartTimeAsc(user.getId());
        return res.stream().map(this::convertToMyReservationResponseDTO).toList();
    }

    public List<ReservationResponseDTO> getAllReservations() {
        List<Reservation> res = reservationRepository.getReservationsByStatusOrderByStartTimeAsc(ReservationStatus.CONFIRMED.value);
        return res.stream().map(this::convertToReservationResponseDTO).toList();
    }

    public MessageResponse cancelMyReservation(Jwt jwt, Long id) {
        User user = getUser(jwt);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!reservation.getUser().getId().equals(user.getId()))
            throw new IllegalAccessError("Reservation does not belong to user");

        reservation.setStatus(ReservationStatus.CANCELLED.value);

        return new MessageResponse("Cancelled reservation: " + reservation.getId());
    }

    public MyReservationResponseDTO postMyReservation(Jwt jwt, GuestReservationRequestDTO reservationRequest) throws ReservationConflict {
        Reservation res = new Reservation();

        res.setService(serviceService.getServiceById(reservationRequest.getServiceId()));
        checkForConflictingReservations(reservationRequest.getStartTime().atZone(ZoneId.of(timezone)), res.getService().getWholeDurationSeconds());
        res.setStatus(ReservationStatus.PENDING.value);
        res.setStartTime(reservationRequest.getStartTime().atZone(ZoneId.of(timezone)));

        User user = getUser(jwt);
        res.setUser(user);
        return convertToMyReservationResponseDTO(reservationRepository.save(res));
    }

    private void checkForConflictingReservations(ZonedDateTime dateTime, Long durationSeconds) throws ReservationConflict {
        if (reservationRepository.existsReservationsByStartTimeIsAfterAndStartTimeBefore(dateTime, dateTime.plusSeconds(durationSeconds))) {
            throw new ReservationConflict("Reservation conflict");
        }
    }

    private ReservationResponseDTO convertToReservationResponseDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setDurationSeconds(reservation.getService().getDurationSeconds());
        dto.setStart(reservation.getStartTime());
        return dto;
    }
    private MyReservationResponseDTO convertToMyReservationResponseDTO(Reservation reservation) {
        MyReservationResponseDTO dto = new MyReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setService(reservation.getService());
        dto.setStatus(reservation.getStatus());
        dto.setStartTime(reservation.getStartTime());
        return dto;
    }

    private User getUser(Jwt jwt) {
        User user;
        try {
            user = userService.getUser(jwt);
        } catch (EntityNotFoundException e) {
            user = userService.syncUserWithKeycloak(jwt);
        }
        return user;
    }


}
