package ch.kenner.maximilian.smartreserve.controller.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ReservationResponseDTO {
    @NotNull
    public Long id;
    @NotNull
    public Long DurationSeconds;
    @NotNull
    public ZonedDateTime Start;
}
