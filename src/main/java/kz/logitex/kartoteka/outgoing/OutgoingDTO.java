package kz.logitex.kartoteka.outgoing;

import kz.logitex.kartoteka.model.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OutgoingDTO {
    private Long id;
    private String documentNumber;
    private String description;
    private String exemplar;
    private Long createdTimestamp;
    private Long estimatedTimestamp;
    private Long closedTimestamp;
    private Long documentTimestamp;
    private Long sendingTimestamp;
    private User userLastUpdated;
    private Status status;
    private Building building;
    private Secret secret;
    private User executor;
    private int copyNumber;
    private int copySheet;
    private int sheet;
    private int schedule;
    private String docDepartmentIndex;
    private int docCopySheet;
    private int docCopyPrint;
    private List<OutgoingStatusHistory> statusHistories;
}
