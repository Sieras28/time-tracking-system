package time.tracking.system.controller;


import time.tracking.system.model.WorkLog;
import time.tracking.system.repository.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/worklogs")
public class AdminWorkLogController {

    @Autowired
    private WorkLogRepository workLogRepository;

    @GetMapping("/pending")
    public String showPendingWorkLogs(Model model) {
        List<WorkLog> pendingWorkLogs = workLogRepository.findByStatus("PENDING");
        model.addAttribute("pendingWorkLogs", pendingWorkLogs);
        return "admin/approve_worklogs";
    }

    @PostMapping("/{id}/approve")
    public String approveWorkLog(@PathVariable Long id) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid work log Id:" + id));
        workLog.setStatus("APPROVED");
        workLogRepository.save(workLog);
        return "redirect:/admin/worklogs/pending";
    }


    @PostMapping("/{id}/reject")
    public String rejectWorkLog(@PathVariable Long id) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid work log Id:" + id));
        workLog.setStatus("REJECTED");
        workLogRepository.save(workLog);
        return "redirect:/admin/worklogs/pending";
    }
}
