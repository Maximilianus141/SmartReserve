package ch.kenner.maximilian.smartreserve.controller.reservation.dto;

import ch.kenner.maximilian.smartreserve.model.reservation.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class AdminReservationRequestDTO {
    @NotNull
    private String userId;
    @NotNull
    private Long serviceId;
    private String status = ReservationStatus.PENDING.value;

    @NotNull
    private ZonedDateTime startTime;
}
