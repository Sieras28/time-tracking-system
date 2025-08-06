package time.tracking.system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalarySummaryDTO {
    private String workerName;
    private double hourlyRate;
    private int totalHours;
    private double totalSalary;
}