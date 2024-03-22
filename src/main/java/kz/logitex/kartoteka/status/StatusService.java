package kz.logitex.kartoteka.status;

import kz.logitex.kartoteka.exception.BadRequestException;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Outgoing;
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

    public Ingoing handleStatusTransitionIngoing(Ingoing ingoing, Status newStatus) {
        if (Objects.requireNonNull(newStatus) == Status.CLOSED) {
            handleClosedStatusIngoing(ingoing);
        } else {
            throw new BadRequestException("Неверный статус: " + newStatus);
        }
        return ingoing;
    }

    private void handleClosedStatusIngoing(Ingoing ingoing) {
        ingoing.setClosedTimestamp(System.currentTimeMillis());
    }

    public Outgoing handleStatusTransitionOutgoing(Outgoing outgoing, Status newStatus) {
        if (Objects.requireNonNull(newStatus) == Status.CLOSED) {
            handleClosedStatusOutgoing(outgoing);
        } else {
            throw new BadRequestException("Неверный статус: " + newStatus);
        }
        return outgoing;
    }

    private void handleClosedStatusOutgoing(Outgoing outgoing) {
        outgoing.setClosedTimestamp(System.currentTimeMillis());
    }
}
