package time.tracking.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportRow {
    private String projectName;
    private String client;
    private String workerName;
    private int totalHours;
    private double hourlyRate;
    private double totalAmount;   // totalHours * hourlyRate
}
