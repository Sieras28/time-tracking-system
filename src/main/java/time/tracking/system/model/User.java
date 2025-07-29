package time.tracking.system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;
    private Double hourlyRate;
    private String role; // ADMIN, WORKER, ACCOUNTANT
    private String name;
    private String surname;
    private String status;

    // Reference to WorkLogs related to the user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WorkLog> workLogs;
}
