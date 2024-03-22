package kz.logitex.kartoteka.status;

import kz.logitex.kartoteka.exception.BadRequestException;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service class responsible for handling ticket-statuses-related operations.
 */
@Service
@RequiredArgsConstructor
public class StatusService {
    public List<Status> getAllStatuses() {
        return Arrays.asList(Status.values());
    }

    public Ingoing handleStatusTransition(Ingoing ingoing, Status newStatus) {
        if (Objects.requireNonNull(newStatus) == Status.CLOSED) {
            handleClosedStatus(ingoing);
        } else {
            throw new BadRequestException("Неверный статус: " + newStatus);
        }
        return ingoing;
    }

    private void handleClosedStatus(Ingoing ingoing) {
        ingoing.setClosedTimestamp(System.currentTimeMillis());
    }
}
