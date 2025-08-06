package time.tracking.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceSummaryDTO {
    private String projectName;
    private String client;
    private int totalHours;
    private double totalInvoice;
}