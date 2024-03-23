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
    private boolean reregistration;
    private List<IngoingStatusHistory> statusHistories;

    public IngoingDTO(Long id, String documentNumber, String description, String resolution, Long createdTimestamp, Long estimatedTimestamp, Long closedTimestamp, Long documentTimestamp, Status status, Building building, Secret secret, int copyNumber, int copySheet, int sheet, int schedule, boolean reregistration) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.description = description;
        this.resolution = resolution;
        this.createdTimestamp = createdTimestamp;
        this.estimatedTimestamp = estimatedTimestamp;
        this.closedTimestamp = closedTimestamp;
        this.documentTimestamp = documentTimestamp;
        this.status = status;
        this.building = building;
        this.secret = secret;
        this.copyNumber = copyNumber;
        this.copySheet = copySheet;
        this.sheet = sheet;
        this.schedule = schedule;
        this.reregistration = reregistration;
    }
}
