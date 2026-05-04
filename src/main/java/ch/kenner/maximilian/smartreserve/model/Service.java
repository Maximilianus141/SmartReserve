package ch.kenner.maximilian.smartreserve.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DialectOverride;

import java.time.Duration;

@Data
@NoArgsConstructor
@Entity
public class Service {

    @Id
    private Long id;

    @Column(nullable = false)
    @Size(max = 255)
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private Long durationSeconds;


    private Long afterServiceBreakDurationSeconds = 0L;
}
