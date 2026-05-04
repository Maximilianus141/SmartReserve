package ch.kenner.maximilian.smartreserve.controller;

import ch.kenner.maximilian.smartreserve.model.ReservationStatus;
import ch.kenner.maximilian.smartreserve.model.Service;
import ch.kenner.maximilian.smartreserve.model.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ReservationRequestDTO {
    @NotNull
    private String userId;
    @NotNull
    private Long serviceId;
    private String status = ReservationStatus.PENDING.value;

    @NotNull
    private ZonedDateTime startTime;
}
