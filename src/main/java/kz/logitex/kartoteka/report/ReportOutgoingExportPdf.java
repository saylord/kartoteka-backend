package kz.logitex.kartoteka.report;

import jakarta.servlet.http.HttpServletResponse;
import kz.logitex.kartoteka.outgoing.OutgoingDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ReportOutgoingExportPdf {
    private final OutgoingDTO outgoing;

    @SneakyThrows
    public void generate(HttpServletResponse response) {

    }
}
