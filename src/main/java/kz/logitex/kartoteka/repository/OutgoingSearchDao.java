package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.outgoing.OutgoingMinDTO;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface OutgoingSearchDao {
    Pair<Page<OutgoingMinDTO>, List<OutgoingMinDTO>> findByStatusIn(String term,
                                                                    Set<Status> statuses,
                                                                    Pageable pageable);
}
