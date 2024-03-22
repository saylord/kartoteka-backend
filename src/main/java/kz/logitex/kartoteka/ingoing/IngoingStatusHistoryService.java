package kz.logitex.kartoteka.ingoing;

import kz.logitex.kartoteka.model.IngoingStatusHistory;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.repository.IngoingStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngoingStatusHistoryService {
    private final IngoingStatusHistoryRepository statusHistoryRepository;

    public void createStatusHistory(Long id, User user, Status statusFrom, Status statusTo) {
        var statusHistory = new IngoingStatusHistory();
        statusHistory.setIngoingId(id);
        statusHistory.setStatusFrom(statusFrom);
        statusHistory.setStatusTo(statusTo);
        statusHistory.setUser(user);
        statusHistory.setTimestamp(System.currentTimeMillis());
        statusHistoryRepository.save(statusHistory);
    }

    public List<IngoingStatusHistory> findAllByIngoingId(Long id) {
        return statusHistoryRepository.findAllByIngoingId(id);
    }
}
