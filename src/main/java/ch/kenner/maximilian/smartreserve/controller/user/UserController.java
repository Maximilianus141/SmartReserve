package ch.kenner.maximilian.smartreserve.controller.user;

import ch.kenner.maximilian.smartreserve.model.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@SecurityRequirement( name = "bearerAuth")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/user/me")
    public ResponseEntity<User> getMe(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.syncUserWithKeycloak(jwt));
    }
}
