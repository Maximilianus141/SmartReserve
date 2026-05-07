package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.controller.service.ServiceService;
import ch.kenner.maximilian.smartreserve.controller.user.UserService;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationStatus;
import ch.kenner.maximilian.smartreserve.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
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
        User user = userService.getUser(jwt);

        List<Reservation> res = reservationRepository.getReservationsByUser_Id(user.getId());
        return res.stream().map(this::convertToMyReservationResponseDTO).toList();
    }

    public List<ReservationResponseDTO> getAllReservations() {
        List<Reservation> res = reservationRepository.getReservationsByStatus(ReservationStatus.CONFIRMED.value);
        return res.stream().map(this::convertToReservationResponseDTO).toList();
    }

    public MyReservationResponseDTO postMyReservation(Jwt jwt, GuestReservationRequestDTO reservationRequest) {
        Reservation res = new Reservation();
        User user = userService.getUser(jwt);
        res.setService(serviceService.getServiceById(reservationRequest.getServiceId()));
        res.setStatus(ReservationStatus.PENDING.value);
        res.setStartTime(reservationRequest.getStartTime().atZone(ZoneId.of(timezone)));
        res.setUser(user);
        return convertToMyReservationResponseDTO(reservationRepository.save(res));
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
