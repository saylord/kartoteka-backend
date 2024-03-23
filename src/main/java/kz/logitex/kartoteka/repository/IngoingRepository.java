package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Ingoing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngoingRepository extends JpaRepository<Ingoing, Long>, IngoingSearchDao {
}
