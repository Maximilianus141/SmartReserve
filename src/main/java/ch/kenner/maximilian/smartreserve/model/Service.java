package ch.kenner.maximilian.smartreserve.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated database ID")
    private Long id;

    @Column(nullable = false)
    @Size(max = 255)
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull(message = "Duration must be provided")
    @Positive(message = "Duration must be greater than zero")
    private Long durationSeconds;

    @PositiveOrZero(message = "Break duration cannot be negative")
    private Long afterServiceBreakDurationSeconds = 0L;
}
