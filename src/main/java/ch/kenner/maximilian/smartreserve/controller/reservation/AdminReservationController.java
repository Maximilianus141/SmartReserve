package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.model.reservation.Reservation;
import ch.kenner.maximilian.smartreserve.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @RolesAllowed(Roles.Admin)
    @GetMapping("api/admin/reservation")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(adminReservationService.getAllReservations());
    }

    @RolesAllowed(Roles.Admin)
    @GetMapping("api/admin/reservation/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminReservationService.getReservationById(id));
    }

    @RolesAllowed(Roles.Admin)
    @PostMapping("api/admin/reservation")
    public ResponseEntity<Reservation> postReservation(@RequestBody AdminReservationRequestDTO reservation) {
        return ResponseEntity.ok(adminReservationService.insertReservation(reservation));
    }

    @RolesAllowed(Roles.Admin)
    @PutMapping("api/admin/reservation/{id}")
    public ResponseEntity<Reservation> putReservation(@PathVariable("id") Long id, @RequestBody AdminReservationRequestDTO reservation) {
        return ResponseEntity.ok(adminReservationService.updateReservation(reservation, id));
    }

    @RolesAllowed(Roles.Admin)
    @DeleteMapping("api/admin/reservation/{id}")
    public ResponseEntity<MessageResponse> deleteReservation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminReservationService.deleteReservation(id));
    }
}
