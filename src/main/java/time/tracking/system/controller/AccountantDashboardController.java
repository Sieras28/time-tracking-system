package time.tracking.system.controller;


import time.tracking.system.dto.InvoiceSummaryDTO;
import time.tracking.system.dto.SalarySummaryDTO;
import time.tracking.system.model.Project;
import time.tracking.system.model.User;
import time.tracking.system.model.WorkLog;
import time.tracking.system.repository.UserRepository;
import time.tracking.system.repository.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard/accountant")
public class AccountantDashboardController {

    @Autowired
    private WorkLogRepository workLogRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showAccountantDashboard(Model model, Principal principal) {
        List<WorkLog> allWorkLogs = workLogRepository.findAll();

        model.addAttribute("allWorkLogs", allWorkLogs);

        model.addAttribute("salarySummary", calculateSalarySummary());
        model.addAttribute("invoiceSummary", calculateInvoiceSummary());

        return "accountant/dashboard";
    }

    private String calculateSalarySummary() {
        double total = workLogRepository.findByStatus("APPROVED")
                .stream()
                .mapToDouble(wl -> {
                    User u = wl.getUser();
                    return (u != null && u.getHourlyRate() != null)
                            ? u.getHourlyRate() * wl.getHours()
                            : 0.0;
                }).sum();
        return "Total Salary: $" + String.format("%.2f", total);
    }


    /* ---------- dashboard tile ----------- */
    private String calculateInvoiceSummary() {
        double total = workLogRepository.findByStatus("APPROVED")
                .stream()
                .mapToDouble(wl -> {
                    User u = wl.getUser();
                    return (u != null && u.getHourlyRate() != null)
                            ? u.getHourlyRate() * wl.getHours()
                            : 0.0;
                }).sum();
        return "Total Invoice: $" + String.format("%.2f", total);
    }


    @GetMapping("/worklogs")
    public String showWorkLogs(Model model) {
        // Fetch all work logs
        List<WorkLog> allWorkLogs = workLogRepository.findAll();
        model.addAttribute("allWorkLogs", allWorkLogs);
        return "accountant/worklogs"; // The template for displaying work logs
    }






    @GetMapping("/salary-summary")
    public String showSalarySummary(Model model) {

        List<User> workers = userRepository.findByRole("WORKER");
        List<SalarySummaryDTO> summaries = new ArrayList<>();

        double grandTotal = 0;

        for (User w : workers) {
            int approvedHours = w.getWorkLogs()
                    .stream()
                    .filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus()))
                    .mapToInt(WorkLog::getHours)
                    .sum();

            double rate = w.getHourlyRate() != null ? w.getHourlyRate() : 0.0;
            double total = rate * approvedHours;

            summaries.add(new SalarySummaryDTO(
                    w.getName() + " " + w.getSurname(),
                    rate,
                    approvedHours,
                    total
            ));
            grandTotal += total;
        }

        model.addAttribute("summaries", summaries);
        model.addAttribute("grandTotal", grandTotal);

        return "accountant/salary-summary";
    }




    @GetMapping("/invoice-summary")
    public String showInvoiceSummary(Model model) {

        // group by project
        Map<Project, InvoiceSummaryDTO> map = new LinkedHashMap<>();

        // only approved logs matter for invoicing
        List<WorkLog> logs = workLogRepository.findByStatus("APPROVED");

        for (WorkLog wl : logs) {
            Project p = wl.getProject();
            if (p == null) continue;

            // hourly rate of the worker who logged the time
            double rate = (wl.getUser() != null && wl.getUser().getHourlyRate() != null)
                    ? wl.getUser().getHourlyRate() : 0.0;

            InvoiceSummaryDTO dto = map.computeIfAbsent(
                    p,
                    pr -> new InvoiceSummaryDTO(pr.getName(), pr.getClient(), 0, 0.0)
            );

            dto.setTotalHours(dto.getTotalHours() + wl.getHours());
            dto.setTotalInvoice(dto.getTotalInvoice() + wl.getHours() * rate);
        }

        double grandTotal = map.values().stream()
                .mapToDouble(InvoiceSummaryDTO::getTotalInvoice)
                .sum();

        model.addAttribute("invoices", map.values());
        model.addAttribute("grandTotal", grandTotal);

        return "accountant/invoice-summary";
    }
}