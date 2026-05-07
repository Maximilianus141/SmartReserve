package ch.kenner.maximilian.smartreserve.controller.reservation;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GuestReservationRequestDTO {
    private Long serviceId;
    private LocalDateTime startTime;
}
