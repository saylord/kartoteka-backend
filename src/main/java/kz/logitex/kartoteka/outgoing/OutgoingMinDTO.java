package kz.logitex.kartoteka.outgoing;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.Secret;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutgoingMinDTO {
    private Long id;
    private String documentNumber;
    private String cardNumber;
    private String description;
    private Status status;
    private User executor;
    private Long createdTimestamp;
    private Long closedTimestamp;
    private Long estimatedTimestamp;
    private Long sendingTimestamp;
    private Building building;
    private Secret secret;
}
