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
        workLog.setUser(user);

        List<Project> myProjects = projectRepository.findAll();

        model.addAttribute("workLog", workLog);
        model.addAttribute("myProjects", myProjects);

        return "worker/worklog_add";
    }

}
