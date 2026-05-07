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


@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
public class GuestReservationController {

    @GetMapping("/api/me/my/reservation")
    @RolesAllowed(Roles.Guest)
    public void getMyReservations(@AuthenticationPrincipal Jwt jwt) {
        getMyReservations(jwt);
    }

}
