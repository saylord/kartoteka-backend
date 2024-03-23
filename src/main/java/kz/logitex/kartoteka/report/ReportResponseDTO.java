package kz.logitex.kartoteka.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponseDTO {
    private String start;
    private String end;
    private Long totalRegistered;
    private Long totalSchedule;
    private Long secretOV;
    private Long secretC;
    private Long secretCC;
    private Long secretCM;
    private Long secretCCM;
    private Long reregistration;
    private Long returnAddress;
    private Long onlyAddress;
}
