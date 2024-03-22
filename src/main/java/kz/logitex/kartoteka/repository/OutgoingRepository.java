package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Outgoing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutgoingRepository extends JpaRepository<Outgoing, Long>, OutgoingSearchDao, JpaSpecificationExecutor<Outgoing> {
}
