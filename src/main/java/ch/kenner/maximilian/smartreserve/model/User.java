package ch.kenner.maximilian.smartreserve.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
    @Id
    private String id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;
}
