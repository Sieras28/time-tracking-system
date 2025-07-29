package time.tracking.system.controller;


import time.tracking.system.model.Project;
import time.tracking.system.model.User;
import time.tracking.system.model.WorkLog;
import time.tracking.system.repository.ProjectRepository;
import time.tracking.system.repository.UserRepository;
import time.tracking.system.repository.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class AdminSummaryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkLogRepository workLogRepository;

    // This will show the summaries page with worker and project dropdown options
    @GetMapping("/admin/summaries")
    public String workerSummaryPage(@RequestParam(required = false) Long workerId,
                                    Model model) {

        // always need the list for the <select>
        List<User> workers = userRepository.findByRole("WORKER");
        model.addAttribute("workers", workers);

        if (workerId != null) {
            // populate summary only when a worker is chosen
            User worker = userRepository.findById(workerId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid worker"));

            List<WorkLog> workLogs = workLogRepository.findByUser(worker);

            int totalHours   = workLogs.stream().mapToInt(WorkLog::getHours).sum();
            long approvedLogs = workLogs.stream().filter(w -> "APPROVED".equalsIgnoreCase(w.getStatus())).count();
            long pendingLogs  = workLogs.stream().filter(w -> "PENDING".equalsIgnoreCase(w.getStatus())).count();

            model.addAttribute("worker", worker);
            model.addAttribute("workLogs", workLogs);
            model.addAttribute("totalHours", totalHours);
            model.addAttribute("approvedLogs", approvedLogs);
            model.addAttribute("pendingLogs", pendingLogs);
        }
        // same view for both states
        return "admin/summaries";
    }


    // View summaries per Worker when selected from the dropdown
    @GetMapping("/admin/summaries/worker")
    public String viewWorkerSummary(@RequestParam Long workerId, Model model) {
        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid worker Id:" + workerId));

        List<WorkLog> workLogs = workLogRepository.findByUser(worker);

        // Calculating total hours worked and number of approved work logs
        int totalHours = 0;
        int approvedLogs = 0;
        int pendingLogs = 0;

        for (WorkLog log : workLogs) {
            totalHours += log.getHours();
            if ("APPROVED".equals(log.getStatus())) {
                approvedLogs++;
            } else if ("PENDING".equals(log.getStatus())) {
                pendingLogs++;
            }
        }

        model.addAttribute("worker", worker);
        model.addAttribute("totalHours", totalHours);
        model.addAttribute("approvedLogs", approvedLogs);
        model.addAttribute("pendingLogs", pendingLogs);
        model.addAttribute("workLogs", workLogs);

        return "admin/worker_summary";
    }

    // View summaries per Project
    @GetMapping("/admin/summaries/project/{projectId}")
    public String viewProjectSummary(@PathVariable Long projectId, Model model) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));

        List<WorkLog> workLogs = workLogRepository.findByProject(project);

        // Calculating total hours worked and number of approved work logs for the project
        int totalHours = 0;
        int approvedLogs = 0;
        int pendingLogs = 0;

        for (WorkLog log : workLogs) {
            totalHours += log.getHours();
            if ("APPROVED".equals(log.getStatus())) {
                approvedLogs++;
            } else if ("PENDING".equals(log.getStatus())) {
                pendingLogs++;
            }
        }

        model.addAttribute("project", project);
        model.addAttribute("totalHours", totalHours);
        model.addAttribute("approvedLogs", approvedLogs);
        model.addAttribute("pendingLogs", pendingLogs);
        model.addAttribute("workLogs", workLogs);

        return "admin/project_summary";
    }
}
