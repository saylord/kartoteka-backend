package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.OutgoingStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutgoingStatusHistoryRepository extends JpaRepository<OutgoingStatusHistory, Long> {
    List<OutgoingStatusHistory> findAllByOutgoingId(Long id);
}
