package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Ingoing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IngoingRepository extends JpaRepository<Ingoing, Long>, IngoingSearchDao, JpaSpecificationExecutor<Ingoing> {
}

