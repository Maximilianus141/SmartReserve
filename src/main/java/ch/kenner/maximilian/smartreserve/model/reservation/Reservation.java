package ch.kenner.maximilian.smartreserve.model.reservation;


import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated database ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    private String status = ReservationStatus.PENDING.value;

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Calculated end time of the reservation")
    private ZonedDateTime endTime;

    @PrePersist
    @PreUpdate
    private void calculateEndTime() {
        endTime = startTime.plusSeconds(service.getWholeDurationSeconds());
    }
}
