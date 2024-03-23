package kz.logitex.kartoteka.report;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping()
    public ResponseEntity<?> getReportList(@RequestBody ReportRequestDTO request) {
        if (request.isIngoing()) return ResponseEntity.ok(reportService.getReportIngoing(request));
        return ResponseEntity.ok(reportService.getReportOutgoing(request));
    }
}
