package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.AdminReservationRequestDTO;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.model.reservation.ReservationRepository;
import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.service.ServiceRepository;
import ch.kenner.maximilian.smartreserve.model.user.User;
import ch.kenner.maximilian.smartreserve.model.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    // TODO: Sort by date
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation insertReservation(AdminReservationRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setService(service);
        reservation.setStatus(dto.getStatus());
        reservation.setStartTime(dto.getStartTime());

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(AdminReservationRequestDTO dto, Long id) {
        return reservationRepository.findById(id)
                .map(reservationOrig -> {
                    reservationOrig.setStatus(dto.getStatus());
                    reservationOrig.setStartTime(dto.getStartTime());
                    return reservationRepository.save(reservationOrig);
                }).orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

    public MessageResponse deleteReservation(Long id) {
        reservationRepository.deleteById(id);
        return new MessageResponse("Reservation " + id + " deleted");
    }
}
