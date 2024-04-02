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
    private String cardNumber;
    private String description;
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
    private String exemplar;
    private int totalSheet;
    private int sheet;
    private int schedule;
    private String docDepartmentIndex;
    private int docCopyPrint;
    private Long reregistrationTimestamp;
    private String caseNumber;
    private String destraction;
    private String nomenclature;
    private Ingoing ingoing;
    private Long interimTimestamp;
    private List<OutgoingStatusHistory> statusHistories;

    public OutgoingDTO(Long id, String documentNumber, String cardNumber, String description, Long createdTimestamp, Long estimatedTimestamp, Long closedTimestamp, Long documentTimestamp, Long sendingTimestamp, Status status, Building building, Secret secret, User executor, String exemplar, int totalSheet, int sheet, int schedule, String docDepartmentIndex, int docCopyPrint, Long reregistrationTimestamp, String caseNumber, String destraction, String nomenclature, Ingoing ingoing, Long interimTimestamp) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.cardNumber = cardNumber;
        this.description = description;
        this.createdTimestamp = createdTimestamp;
        this.estimatedTimestamp = estimatedTimestamp;
        this.closedTimestamp = closedTimestamp;
        this.documentTimestamp = documentTimestamp;
        this.sendingTimestamp = sendingTimestamp;
        this.status = status;
        this.building = building;
        this.secret = secret;
        this.executor = executor;
        this.exemplar = exemplar;
        this.totalSheet = totalSheet;
        this.sheet = sheet;
        this.schedule = schedule;
        this.docDepartmentIndex = docDepartmentIndex;
        this.docCopyPrint = docCopyPrint;
        this.reregistrationTimestamp = reregistrationTimestamp;
        this.caseNumber = caseNumber;
        this.destraction = destraction;
        this.nomenclature = nomenclature;
        this.ingoing = ingoing;
        this.interimTimestamp = interimTimestamp;
    }
}
