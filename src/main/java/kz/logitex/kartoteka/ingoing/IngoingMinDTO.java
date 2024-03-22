package kz.logitex.kartoteka.ingoing;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.Secret;
import kz.logitex.kartoteka.model.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngoingMinDTO {
    private Long id;
    private String documentNumber;
    private Status status;
    private Long createdTimestamp;
    private Long closedTimestamp;
    private Long estimatedTimestamp;
    private Building building;
    private Secret secret;
}
