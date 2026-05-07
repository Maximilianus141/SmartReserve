package ch.kenner.maximilian.smartreserve.controller.availability;


import ch.kenner.maximilian.smartreserve.base.TimeFrame;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("/api/availability")
@Validated
public class AvailabilityController {

    @GetMapping("/{date}")
    public ResponseEntity<List<TimeFrame>> getAvailability(@PathVariable LocalDate date) {
        return ResponseEntity.ok(List.of());
    }
}
