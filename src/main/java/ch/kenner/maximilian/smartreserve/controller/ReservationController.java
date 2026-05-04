package ch.kenner.maximilian.smartreserve.controller;

import ch.kenner.maximilian.smartreserve.model.Reservation;
import ch.kenner.maximilian.smartreserve.model.ReservationRepository;
import ch.kenner.maximilian.smartreserve.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RolesAllowed(Roles.Admin)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("api/reservation")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("api/reservation/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathParam("id") Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping("api/reservation")
    public ResponseEntity<Reservation> postReservation(ReservationRequestDTO reservation) {
        return ResponseEntity.ok(reservationService.insertReservation(reservation));
    }

    @PutMapping("api/reservation/{id}")
    public ResponseEntity<Reservation> putReservation(@PathParam("id") Long id, @RequestBody ReservationRequestDTO reservation) {
        return ResponseEntity.ok(reservationService.updateReservation(reservation, id));
    }


}
