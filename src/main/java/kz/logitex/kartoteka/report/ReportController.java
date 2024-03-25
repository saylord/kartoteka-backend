package kz.logitex.kartoteka.report;

import jakarta.servlet.http.HttpServletResponse;
import kz.logitex.kartoteka.ingoing.IngoingService;
import kz.logitex.kartoteka.outgoing.OutgoingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private IngoingService ingoingService;
    @Autowired
    private OutgoingService outgoingService;

    @PostMapping()
    public ResponseEntity<?> getReportList(@RequestBody ReportRequestDTO request) {
        if (request.isIngoing()) return ResponseEntity.ok(reportService.getReportIngoing(request));
        return ResponseEntity.ok(reportService.getReportOutgoing(request));
    }

    @GetMapping("/pdf/{id}")
    @SneakyThrows
    public void generatePdfFileById(HttpServletResponse response,
                                    @PathVariable(value = "id") Long id,
                                    @RequestParam(defaultValue = "true") boolean ingoing) {
        response.setContentType("application/pdf");
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        var currentDateTime = dateFormat.format(new Date());
        var headerKey = "Content-Disposition";
        var headerValue = "attachment; filename=file" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        if (ingoing) {
            var res = ingoingService.getIngoingById(id);
            var generator = new ReportIngoingExportPdf(res);
            generator.generate(response);
        } else {
            var res = outgoingService.getOutgoingById(id);
            var generator = new ReportOutgoingExportPdf(res);
            generator.generate(response);
        }
    }
}
