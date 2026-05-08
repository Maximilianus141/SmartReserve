package ch.kenner.maximilian.smartreserve.controller.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ReservationResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long durationSeconds;
    @NotNull
    private ZonedDateTime startTime;
    @NotNull
    private ZonedDateTime endTime;
}
