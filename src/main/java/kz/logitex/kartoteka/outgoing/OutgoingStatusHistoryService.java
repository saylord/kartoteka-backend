package kz.logitex.kartoteka.outgoing;

import kz.logitex.kartoteka.model.OutgoingStatusHistory;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.repository.OutgoingStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutgoingStatusHistoryService {
    private final OutgoingStatusHistoryRepository statusHistoryRepository;

    public void createStatusHistory(Long id, User user, Status statusFrom, Status statusTo) {
        var statusHistory = new OutgoingStatusHistory();
        statusHistory.setOutgoingId(id);
        statusHistory.setStatusFrom(statusFrom);
        statusHistory.setStatusTo(statusTo);
        statusHistory.setUser(user);
        statusHistory.setTimestamp(System.currentTimeMillis());
        statusHistoryRepository.save(statusHistory);
    }

    public List<OutgoingStatusHistory> findAllByOutgoingId(Long id) {
        return statusHistoryRepository.findAllByOutgoingId(id);
    }
}
