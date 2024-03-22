package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.ingoing.IngoingMinDTO;
import kz.logitex.kartoteka.model.Status;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IngoingSearchDao {
    Pair<Page<IngoingMinDTO>, List<IngoingMinDTO>> findByStatusIn(String term,
                                                                  Set<Status> statuses,
                                                                  Pageable pageable);
}
