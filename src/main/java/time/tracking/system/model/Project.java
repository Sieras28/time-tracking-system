package time.tracking.system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String client;
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;

    // Reference to the 'User' entity instead of 'Worker'
    @ManyToOne
    @JoinColumn(name = "user_id")  // Changed from 'worker_id' to 'user_id'
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<WorkLog> workLogs;
}
