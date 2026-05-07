package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.controller.user.UserService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
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

    private MyReservationResponseDTO convertToMyReservationResponseDTO(Reservation reservation) {
        MyReservationResponseDTO dto = new MyReservationResponseDTO();
        dto.setService(reservation.getService());
        dto.setStatus(reservation.getStatus());
        dto.setStartTime(reservation.getStartTime());
        return dto;
    }


}
