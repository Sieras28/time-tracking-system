package time.tracking.system.controller;

import time.tracking.system.model.User;
import time.tracking.system.model.Project;
import time.tracking.system.model.WorkLog;
import time.tracking.system.repository.UserRepository;
import time.tracking.system.repository.ProjectRepository;
import time.tracking.system.repository.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class WorkerDashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkLogRepository workLogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/worker/dashboard")
    public String showWorkerDashboard(Model model, Principal principal) {
        String username = principal.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);

        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        List<Project> myProjects = projectRepository.findByUser(user);
        List<WorkLog> myWorkLogs = workLogRepository.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("myProjects", myProjects);
        model.addAttribute("myWorkLogs", myWorkLogs);

        return "worker/dashboard";
    }


    @GetMapping("/worker/worklogs")
    public String showWorkerWorkLogs(Model model, Principal principal) {
        String username = principal.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        List<WorkLog> myWorkLogs = workLogRepository.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("myWorkLogs", myWorkLogs);

        return "worker/worker-worklogs"; // This should match your Thymeleaf template location (worker/worklogs.html)
    }

}
