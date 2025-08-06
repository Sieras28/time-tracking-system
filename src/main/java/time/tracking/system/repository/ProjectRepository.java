package time.tracking.system.repository;

import time.tracking.system.model.Project;
import time.tracking.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByUser_Id(Long userId);
    List<Project> findByUser(User user);

}
