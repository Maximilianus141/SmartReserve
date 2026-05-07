package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public List<MyReservationResponseDTO> getMyReservations(@AuthenticationPrincipal Jwt jwt) {
        return guestReservationService.getMyReservations(jwt);
    }

    @GetMapping("/api/reservations")
    @RolesAllowed(Roles.Guest)
    public List<ReservationResponseDTO> getReservations(){
        return guestReservationService.getAllReservations();
    }

}
