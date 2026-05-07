package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.controller.user.UserService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationStatus;
import ch.kenner.maximilian.smartreserve.model.user.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    GuestReservationService(UserService userService, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.reservationRepository = reservationRepository;
    }

    public List<MyReservationResponseDTO> getMyReservations(Jwt jwt) {
        User user = userService.getUser(jwt);

        List<Reservation> res = reservationRepository.getReservationsByUser_Id(user.getId());
        return res.stream().map(this::convertToMyReservationResponseDTO).toList();
    }

    public List<ReservationResponseDTO> getAllReservations() {
        List<Reservation> res = reservationRepository.getReservationsByStatus(ReservationStatus.CONFIRMED.value);
        return res.stream().map(this::convertToReservationResponseDTO).toList();
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


}
