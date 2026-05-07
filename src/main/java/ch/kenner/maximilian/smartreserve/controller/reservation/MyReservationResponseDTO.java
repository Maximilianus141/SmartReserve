package ch.kenner.maximilian.smartreserve.controller.reservation;

import ch.kenner.maximilian.smartreserve.model.reservation.ReservationStatus;
import ch.kenner.maximilian.smartreserve.model.service.Service;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
