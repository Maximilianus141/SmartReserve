package ch.kenner.maximilian.smartreserve.controller.reservation.dto;

import ch.kenner.maximilian.smartreserve.model.service.Service;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MyReservationResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private Service service;
    @NotNull
    private String status;
    @NotNull
    private ZonedDateTime startTime;
}
