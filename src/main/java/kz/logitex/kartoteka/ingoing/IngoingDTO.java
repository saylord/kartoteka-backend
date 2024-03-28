package kz.logitex.kartoteka.ingoing;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.IngoingStatusHistory;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.model.Secret;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class IngoingDTO {
    private Long id;
    private String documentNumber;
    private String cardNumber;
    private String description;
    private String resolution;
    private Long createdTimestamp;
    private Long estimatedTimestamp;
    private Long closedTimestamp;
    private Long documentTimestamp;
    private User executor;
    private User userLastUpdated;
    private Status status;
    private Building building;
    private Secret secret;
    private String exemplar;
    private int totalSheet;
    private int sheet;
    private int schedule;
    private Long reregistrationTimestamp;
    private String caseNumber;
    private String nomenclature;
    private List<IngoingStatusHistory> statusHistories;

    public IngoingDTO(Long id, String documentNumber, String cardNumber, String description, String resolution, Long createdTimestamp, Long estimatedTimestamp, Long closedTimestamp, Long documentTimestamp, User executor, Status status, Building building, Secret secret, String exemplar, int totalSheet, int sheet, int schedule, Long reregistrationTimestamp, String caseNumber, String nomenclature) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.cardNumber = cardNumber;
        this.description = description;
        this.resolution = resolution;
        this.createdTimestamp = createdTimestamp;
        this.estimatedTimestamp = estimatedTimestamp;
        this.closedTimestamp = closedTimestamp;
        this.documentTimestamp = documentTimestamp;
        this.executor = executor;
        this.status = status;
        this.building = building;
        this.secret = secret;
        this.exemplar = exemplar;
        this.totalSheet = totalSheet;
        this.sheet = sheet;
        this.schedule = schedule;
        this.reregistrationTimestamp = reregistrationTimestamp;
        this.caseNumber = caseNumber;
        this.nomenclature = nomenclature;
    }
}
