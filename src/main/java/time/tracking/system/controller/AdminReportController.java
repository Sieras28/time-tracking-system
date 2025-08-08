package time.tracking.system.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import time.tracking.system.dto.ReportRow;
import time.tracking.system.repository.WorkLogRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    @Autowired
    private WorkLogRepository workLogRepository;


    @GetMapping
    public String showReportPage(Model model) {
        model.addAttribute("rows", buildRows());
        return "admin/report";
    }


    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> downloadPdf() throws Exception {

        List<ReportRow> rows = buildRows();


        Document doc = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);
        doc.open();

        ClassPathResource logoFile = new ClassPathResource("static/images/company-logo.png");
        Image logo = Image.getInstance(logoFile.getInputStream().readAllBytes());
        logo.scaleToFit(120, 60);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("", headerFont);

        float logoW   = logo.getScaledWidth();
        float pageW   = doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin();

        PdfPTable header = new PdfPTable(new float[]{1, 4, 1});
        header.setWidthPercentage(100);
        header.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell logoCell = new PdfPCell(logo, true);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        header.addCell(logoCell);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.addElement(title);
        header.addCell(titleCell);

        PdfPCell spacer = new PdfPCell();
        spacer.setBorder(Rectangle.NO_BORDER);
        header.addCell(spacer);

        doc.add(header);
        doc.add(Chunk.NEWLINE);

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 3, 3, 2, 2, 2});

        String[] headers = {"Project", "Client", "Worker", "Hours", "Rate", "Amount"};
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, headerFont));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        for (ReportRow r : rows) {
            table.addCell(new Phrase(r.getProjectName(), cellFont));
            table.addCell(new Phrase(r.getClient(), cellFont));
            table.addCell(new Phrase(r.getWorkerName(), cellFont));
            table.addCell(new Phrase(String.valueOf(r.getTotalHours()), cellFont));
            table.addCell(new Phrase(String.format("%.2f", r.getHourlyRate()), cellFont));
            table.addCell(new Phrase(String.format("%.2f", r.getTotalAmount()), cellFont));
        }

        doc.add(table);
        doc.close();

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=client-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    private List<ReportRow> buildRows() {
        List<ReportRow> rows = new ArrayList<>();
        workLogRepository.findByStatus("APPROVED").forEach(wl -> {
            double rate = wl.getUser() != null && wl.getUser().getHourlyRate() != null
                    ? wl.getUser().getHourlyRate() : 0.0;
            rows.add(new ReportRow(
                    wl.getProject().getName(),
                    wl.getProject().getClient(),
                    wl.getUser().getName() + " " + wl.getUser().getSurname(),
                    wl.getHours(),
                    rate,
                    wl.getHours() * rate
            ));
        });
        return rows;
    }
}
