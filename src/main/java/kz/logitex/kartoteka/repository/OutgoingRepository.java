package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Outgoing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutgoingRepository extends JpaRepository<Outgoing, Long>, OutgoingSearchDao {
}
