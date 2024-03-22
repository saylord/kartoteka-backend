package kz.logitex.kartoteka.ingoing;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.IngoingStatusHistory;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.model.Secret;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IngoingDTO {
    private Long id;
    private String documentNumber;
    private String description;
    private String resolution;
    private Long createdTimestamp;
    private Long estimatedTimestamp;
    private Long closedTimestamp;
    private Long documentTimestamp;
    private User userLastUpdated;
    private Status status;
    private Building building;
    private Secret secret;
    private int copyNumber;
    private int copySheet;
    private int sheet;
    private int schedule;
    private String docDepartmentIndex;
    private int docCopySheet;
    private int docCopyPrint;
    private List<IngoingStatusHistory> statusHistories;
}
