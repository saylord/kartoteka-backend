package kz.logitex.kartoteka.outgoing;

import kz.logitex.kartoteka.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
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
    private boolean reregistration;
    private boolean returnAddress;
    private boolean onlyAddress;

    public OutgoingDTO(Long id, String documentNumber, String description, String exemplar, Long createdTimestamp, Long estimatedTimestamp, Long closedTimestamp, Long documentTimestamp, Long sendingTimestamp, Status status, Building building, Secret secret, User executor, int copyNumber, int copySheet, int sheet, int schedule, String docDepartmentIndex, int docCopySheet, int docCopyPrint, boolean reregistration, boolean returnAddress, boolean onlyAddress) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.description = description;
        this.exemplar = exemplar;
        this.createdTimestamp = createdTimestamp;
        this.estimatedTimestamp = estimatedTimestamp;
        this.closedTimestamp = closedTimestamp;
        this.documentTimestamp = documentTimestamp;
        this.sendingTimestamp = sendingTimestamp;
        this.status = status;
        this.building = building;
        this.secret = secret;
        this.executor = executor;
        this.copyNumber = copyNumber;
        this.copySheet = copySheet;
        this.sheet = sheet;
        this.schedule = schedule;
        this.docDepartmentIndex = docDepartmentIndex;
        this.docCopySheet = docCopySheet;
        this.docCopyPrint = docCopyPrint;
        this.reregistration = reregistration;
        this.returnAddress = returnAddress;
        this.onlyAddress = onlyAddress;
    }
}
