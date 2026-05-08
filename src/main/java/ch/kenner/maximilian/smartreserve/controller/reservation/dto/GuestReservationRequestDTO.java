package ch.kenner.maximilian.smartreserve.controller.reservation.dto;


import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GuestReservationRequestDTO {
    private Long serviceId;
    private ZonedDateTime startTime;
}
