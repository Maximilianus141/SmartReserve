package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.base.ReservationConflict;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.GuestReservationRequestDTO;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.MyReservationResponseDTO;
import ch.kenner.maximilian.smartreserve.controller.reservation.dto.ReservationResponseDTO;
import ch.kenner.maximilian.smartreserve.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
public class GuestReservationController {


    private final GuestReservationService guestReservationService;

    public GuestReservationController(GuestReservationService guestReservationService) {
        this.guestReservationService = guestReservationService;
    }


    @GetMapping("/api/me/reservations")
    @RolesAllowed(Roles.Guest)
    public ResponseEntity<List<MyReservationResponseDTO>> getMyReservations(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(guestReservationService.getMyReservations(jwt));
    }

    @GetMapping("/api/reservations")
    @RolesAllowed(Roles.Guest)
    public ResponseEntity<List<ReservationResponseDTO>> getReservations(){
        return ResponseEntity.ok(guestReservationService.getAllReservations());
    }

    @PostMapping("/api/me/reservation")
    @RolesAllowed(Roles.Guest)
    public ResponseEntity<MyReservationResponseDTO> postMyReservation(@AuthenticationPrincipal Jwt jwt, @RequestBody GuestReservationRequestDTO reservation) {
        try {
            return ResponseEntity.ok(guestReservationService.postMyReservation(jwt, reservation));
        } catch (ReservationConflict e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/api/me/reservation/{id}/cancel")
    @RolesAllowed(Roles.Guest)
    public ResponseEntity<MessageResponse> cancelMyReservation(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(guestReservationService.cancelMyReservation(jwt, id));
        } catch (IllegalAccessError e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
