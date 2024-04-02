package kz.logitex.kartoteka.ingoing;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.Secret;
import kz.logitex.kartoteka.model.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngoingMinDTO {
    private Long id;
    private String documentNumber;
    private Status status;
    private Long createdTimestamp;
    private Long closedTimestamp;
    private Long estimatedTimestamp;
    private Building building;
    private Secret secret;
    private Long annualTimestamp;
    private Long semiAnnualTimestamp;
    private Long monthlyTimestamp;
    private Long tenDayTimestamp;
    private boolean annual;
    private boolean semiAnnual;
    private boolean monthly;
    private boolean tenDay;
}
