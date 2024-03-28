package kz.logitex.kartoteka.ingoing;

import kz.logitex.kartoteka.exception.NotFoundException;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.repository.IngoingRepository;
import kz.logitex.kartoteka.repository.UserRepository;
import kz.logitex.kartoteka.status.StatusService;
import kz.logitex.kartoteka.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling ingoing-related operations.
 */
@Service
@RequiredArgsConstructor
public class IngoingService {
    private final IngoingRepository ingoingRepository;
    private final UserRepository userRepository;
    private final StatusService statusService;
    private final IngoingStatusHistoryService statusHistoryService;
    private final AuthUtil authUtil;

    public Ingoing createIngoing(Ingoing request) {
        var createdTimestamp = System.currentTimeMillis();

        var ingoing = Ingoing.builder()
                .documentNumber(request.getDocumentNumber())
                .description(request.getDescription())
                .resolution(request.getResolution())
                .createdTimestamp(createdTimestamp)
                .estimatedTimestamp(request.getEstimatedTimestamp())
                .documentTimestamp(request.getDocumentTimestamp())
                .executor(request.getExecutor())
                .status(Status.OPENED)
                .building(request.getBuilding())
                .secret(request.getSecret())
                .exemplar(request.getExemplar())
                .totalSheet(request.getTotalSheet())
                .sheet(request.getSheet())
                .schedule(request.getSchedule())
                .reregistrationTimestamp(request.getReregistrationTimestamp())
                .caseNumber(request.getCaseNumber())
                .nomenclature(request.getNomenclature())
                .build();
        var res = ingoingRepository.save(ingoing);
        res.setCardNumber(res.getId() + " " + res.getSecret().getName());
        return res;
    }

    public IngoingDTO getIngoingById(Long id) {
        var ingoing = ingoingRepository.findById(id).orElseThrow(() -> new NotFoundException("Входящий не найден с айди: " + id));
        var statusHistories = statusHistoryService.findAllByIngoingId(ingoing.getId());
        return IngoingDTO.builder()
                .id(ingoing.getId())
                .documentNumber(ingoing.getDocumentNumber())
                .cardNumber(ingoing.getCardNumber())
                .description(ingoing.getDescription())
                .resolution(ingoing.getResolution())
                .createdTimestamp(ingoing.getCreatedTimestamp())
                .closedTimestamp(ingoing.getClosedTimestamp())
                .estimatedTimestamp(ingoing.getEstimatedTimestamp())
                .documentTimestamp(ingoing.getDocumentTimestamp())
                .status(ingoing.getStatus())
                .executor(ingoing.getExecutor())
                .statusHistories(statusHistories)
                .building(ingoing.getBuilding())
                .userLastUpdated(ingoing.getUserLastUpdated())
                .secret(ingoing.getSecret())
                .exemplar(ingoing.getExemplar())
                .totalSheet(ingoing.getTotalSheet())
                .sheet(ingoing.getSheet())
                .schedule(ingoing.getSchedule())
                .reregistrationTimestamp(ingoing.getReregistrationTimestamp())
                .caseNumber(ingoing.getCaseNumber())
                .nomenclature(ingoing.getNomenclature())
                .build();
    }

    public IngoingLotDTO getAllIngoingsByStatus(int page, int size, Set<Status> status, String sort, boolean asc, String term) {
        var sorting = asc ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable paging = PageRequest.of(page, size, sorting);

        Set<Status> statusSet = (status != null) ? status : EnumSet.allOf(Status.class);

        var pageIngoings = ingoingRepository.findByStatusIn(
                term, statusSet, paging
        );
        return getIngoingLotDTO(pageIngoings.a, pageIngoings.b);
    }

    private IngoingLotDTO getIngoingLotDTO(Page<IngoingMinDTO> pageIngoings, List<IngoingMinDTO> list) {
        Map<Status, Long> statusCounts = list.stream()
                .collect(Collectors.groupingBy(IngoingMinDTO::getStatus, Collectors.counting()));

        var totalExpired = list.stream()
                .filter(ingoing ->
                        ingoing.getEstimatedTimestamp() != null &&
                                ingoing.getStatus() != Status.CLOSED &&
                                System.currentTimeMillis() > ingoing.getEstimatedTimestamp())
                .count();

        return IngoingLotDTO.builder()
                .ingoings(pageIngoings.getContent())
                .currentPage(pageIngoings.getNumber())
                .totalItems(pageIngoings.getTotalElements())
                .totalPages(pageIngoings.getTotalPages())
                .totalDocuments(list.size())
                .totalOpened(statusCounts.getOrDefault(Status.OPENED, 0L))
                .totalClosed(statusCounts.getOrDefault(Status.CLOSED, 0L))
                .totalExpired(totalExpired)
                .build();
    }

    public Ingoing updateIngoing(Long id, Ingoing request) {
        var ingoing = ingoingRepository.findById(id).orElseThrow(() -> new NotFoundException("Входящий не найден с айди: " + id));
        var currentUser = userRepository.findById(authUtil.getAuth().getUserId()).get();
        if (ingoing.getStatus() != request.getStatus()) {
            ingoing = statusService.handleStatusTransitionIngoing(ingoing, request.getStatus());
            statusHistoryService.createStatusHistory(
                    id,
                    currentUser,
                    ingoing.getStatus(),
                    request.getStatus()
            );
        }
        ingoing.setDocumentNumber(request.getDocumentNumber());
        ingoing.setCardNumber(request.getId() + " " + request.getSecret().getName());
        ingoing.setDescription(request.getDescription());
        ingoing.setResolution(request.getResolution());
        ingoing.setDocumentTimestamp(request.getDocumentTimestamp());
        ingoing.setUserLastUpdated(currentUser);
        ingoing.setExecutor(request.getExecutor());
        ingoing.setStatus(request.getStatus());
        ingoing.setBuilding(request.getBuilding());
        ingoing.setSecret(request.getSecret());
        ingoing.setExemplar(request.getExemplar());
        ingoing.setTotalSheet(request.getTotalSheet());
        ingoing.setSheet(request.getSheet());
        ingoing.setSchedule(request.getSchedule());
        ingoing.setReregistrationTimestamp(request.getReregistrationTimestamp());
        ingoing.setCaseNumber(request.getCaseNumber());
        ingoing.setNomenclature(request.getNomenclature());

        return ingoingRepository.save(ingoing);
    }

    public Long deleteIngoing(Long id) {
        var ingoing = ingoingRepository.findById(id).orElseThrow(() -> new NotFoundException("Вхоядщий не найден с именем пользователя: " + id));
        ingoingRepository.delete(ingoing);
        return id;
    }
}
