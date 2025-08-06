package time.tracking.system.repository;

import time.tracking.system.model.Project;
import time.tracking.system.model.User;
import time.tracking.system.model.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog,Long> {
    List<WorkLog> findAllByUser_Id(Long userId);
    List<WorkLog> findByUser(User user);
    List<WorkLog> findByStatus(String status);
    List<WorkLog> findByProject(Project project);
}
