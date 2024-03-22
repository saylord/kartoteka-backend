package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.IngoingStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngoingStatusHistoryRepository extends JpaRepository<IngoingStatusHistory, Long> {
    List<IngoingStatusHistory> findAllByIngoingId(Long id);
}
