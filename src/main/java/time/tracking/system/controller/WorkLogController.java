package time.tracking.system.controller;

import time.tracking.system.model.Project;
import time.tracking.system.model.WorkLog;
import time.tracking.system.model.User;
import time.tracking.system.repository.ProjectRepository;
import time.tracking.system.repository.WorkLogRepository;
import time.tracking.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/worker/worklogs")
public class WorkLogController {

    @Autowired
    private WorkLogRepository workLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;


//    @GetMapping("/add")
//    public String showAddWorkLogForm(Model model, Principal principal) {
//        String username = principal.getName();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        WorkLog workLog = new WorkLog();
//        workLog.setUser(user); // Assign the logged-in user to the work log
//
//        model.addAttribute("workLog", workLog);
//        return "worker/worklog_add"; // Thymeleaf template for adding work logs
//    }

    // Handle Work Log Submission
    @PostMapping("/add")
    public String addWorkLog(@ModelAttribute WorkLog workLog, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "worker/worklog_add";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        workLog.setUser(user);
        workLog.setStatus("PENDING");
        workLogRepository.save(workLog);

        return "redirect:/worker/dashboard";
    }


    @GetMapping("/add")
    public String showAddWorkLogForm(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        WorkLog workLog = new WorkLog();
        workLog.setUser(user); // Associate the logged-in user with the work log

        // Retrieve projects associated with the logged-in user
        List<Project> myProjects = projectRepository.findAll();

        // Add work log and projects to the model
        model.addAttribute("workLog", workLog);
        model.addAttribute("myProjects", myProjects);

        return "worker/worklog_add";  // Thymeleaf template for adding work logs
    }

}
