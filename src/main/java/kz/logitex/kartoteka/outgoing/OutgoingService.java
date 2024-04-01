package kz.logitex.kartoteka.outgoing;

import kz.logitex.kartoteka.exception.NotFoundException;
import kz.logitex.kartoteka.ingoing.IngoingDTO;
import kz.logitex.kartoteka.ingoing.IngoingLotDTO;
import kz.logitex.kartoteka.ingoing.IngoingMinDTO;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Outgoing;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.repository.OutgoingRepository;
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

@Service
@RequiredArgsConstructor
public class OutgoingService {
    private final OutgoingRepository outgoingRepository;
    private final UserRepository userRepository;
    private final StatusService statusService;
    private final OutgoingStatusHistoryService statusHistoryService;
    private final AuthUtil authUtil;

    public Outgoing createOutgoing(Outgoing request) {
        var createdTimestamp = System.currentTimeMillis();

        var outgoing = Outgoing.builder()
                .documentNumber(request.getDocumentNumber())
                .description(request.getDescription())
                .exemplar(request.getExemplar())
                .createdTimestamp(createdTimestamp)
                .estimatedTimestamp(request.getEstimatedTimestamp())
                .documentTimestamp(request.getDocumentTimestamp())
                .sendingTimestamp(request.getSendingTimestamp())
                .status(Status.OPENED)
                .executor(request.getExecutor())
                .building(request.getBuilding())
                .secret(request.getSecret())
                .totalSheet(request.getSheet()+request.getSchedule())
                .sheet(request.getSheet())
                .schedule(request.getSchedule())
                .docDepartmentIndex(request.getDocDepartmentIndex())
                .docCopyPrint(request.getDocCopyPrint())
                .reregistrationTimestamp(request.getReregistrationTimestamp())
                .caseNumber(request.getCaseNumber())
                .destraction(request.getDestraction())
                .nomenclature(request.getNomenclature())
                .build();

        var res = outgoingRepository.save(outgoing);
        res.setCardNumber(res.getId() + " " + res.getSecret().getName());
        return res;
    }

    public OutgoingDTO getOutgoingById(Long id) {
        var outgoing = outgoingRepository.findById(id).orElseThrow(() -> new NotFoundException("Исходящий не найден с айди: " + id));
        var statusHistories = statusHistoryService.findAllByOutgoingId(outgoing.getId());
        return OutgoingDTO.builder()
                .id(outgoing.getId())
                .documentNumber(outgoing.getDocumentNumber())
                .cardNumber(outgoing.getCardNumber())
                .description(outgoing.getDescription())
                .exemplar(outgoing.getExemplar())
                .executor(outgoing.getExecutor())
                .createdTimestamp(outgoing.getCreatedTimestamp())
                .closedTimestamp(outgoing.getClosedTimestamp())
                .estimatedTimestamp(outgoing.getEstimatedTimestamp())
                .documentTimestamp(outgoing.getDocumentTimestamp())
                .sendingTimestamp(outgoing.getSendingTimestamp())
                .status(outgoing.getStatus())
                .statusHistories(statusHistories)
                .building(outgoing.getBuilding())
                .userLastUpdated(outgoing.getUserLastUpdated())
                .secret(outgoing.getSecret())
                .totalSheet(outgoing.getTotalSheet())
                .sheet(outgoing.getSheet())
                .schedule(outgoing.getSchedule())
                .docDepartmentIndex(outgoing.getDocDepartmentIndex())
                .docCopyPrint(outgoing.getDocCopyPrint())
                .reregistrationTimestamp(outgoing.getReregistrationTimestamp())
                .caseNumber(outgoing.getCaseNumber())
                .destraction(outgoing.getDescription())
                .nomenclature(outgoing.getNomenclature())
                .build();
    }

    public OutgoingLotDTO getAllOutgoingsByStatus(int page, int size, Set<Status> status, String sort, boolean asc, String term) {
        var sorting = asc ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable paging = PageRequest.of(page, size, sorting);

        Set<Status> statusSet = (status != null) ? status : EnumSet.allOf(Status.class);

        var pageOutgoings = outgoingRepository.findByStatusIn(
                term, statusSet, paging
        );
        return getOutgoingLotDTO(pageOutgoings.a, pageOutgoings.b);
    }

    private OutgoingLotDTO getOutgoingLotDTO(Page<OutgoingMinDTO> pageOutgoings, List<OutgoingMinDTO> list) {
        Map<Status, Long> statusCounts = list.stream()
                .collect(Collectors.groupingBy(OutgoingMinDTO::getStatus, Collectors.counting()));

        var totalExpired = list.stream()
                .filter(outgoing ->
                        outgoing.getEstimatedTimestamp() != null &&
                                outgoing.getStatus() != Status.CLOSED &&
                                System.currentTimeMillis() > outgoing.getEstimatedTimestamp())
                .count();

        return OutgoingLotDTO.builder()
                .outgoings(pageOutgoings.getContent())
                .currentPage(pageOutgoings.getNumber())
                .totalItems(pageOutgoings.getTotalElements())
                .totalPages(pageOutgoings.getTotalPages())
                .totalDocuments(list.size())
                .totalOpened(statusCounts.getOrDefault(Status.OPENED, 0L))
                .totalClosed(statusCounts.getOrDefault(Status.CLOSED, 0L))
                .totalExpired(totalExpired)
                .build();
    }

    public Outgoing updateOutgoing(Long id, Outgoing request) {
        var outgoing = outgoingRepository.findById(id).orElseThrow(() -> new NotFoundException("Исходящий не найден с айди: " + id));
        var currentUser = userRepository.findById(authUtil.getAuth().getUserId()).get();
        if (outgoing.getStatus() != request.getStatus()) {
            outgoing = statusService.handleStatusTransitionOutgoing(outgoing, request.getStatus());
            statusHistoryService.createStatusHistory(
                    id,
                    currentUser,
                    outgoing.getStatus(),
                    request.getStatus()
            );
        }
        outgoing.setDocumentNumber(request.getDocumentNumber());
        outgoing.setCardNumber(request.getId() + " " + request.getSecret().getName());
        outgoing.setDescription(request.getDescription());
        outgoing.setExemplar(request.getExemplar());
        outgoing.setDocumentTimestamp(request.getDocumentTimestamp());
        outgoing.setSendingTimestamp(request.getSendingTimestamp());
        outgoing.setExecutor(request.getExecutor());
        outgoing.setUserLastUpdated(currentUser);
        outgoing.setStatus(request.getStatus());
        outgoing.setBuilding(request.getBuilding());
        outgoing.setSecret(request.getSecret());
        outgoing.setTotalSheet(request.getSheet()+request.getSchedule());
        outgoing.setSheet(request.getSheet());
        outgoing.setSchedule(request.getSchedule());
        outgoing.setDocDepartmentIndex(request.getDocDepartmentIndex());
        outgoing.setDocCopyPrint(request.getDocCopyPrint());
        outgoing.setReregistrationTimestamp(request.getReregistrationTimestamp());
        outgoing.setCaseNumber(request.getCaseNumber());
        outgoing.setDescription(request.getDescription());
        outgoing.setNomenclature(request.getNomenclature());
        outgoing.setEstimatedTimestamp(request.getEstimatedTimestamp());

        return outgoingRepository.save(outgoing);
    }

    public Long deleteOutgoing(Long id) {
        var outgoing = outgoingRepository.findById(id).orElseThrow(() -> new NotFoundException("Исходящий не найден с именем пользователя: " + id));
        outgoingRepository.delete(outgoing);
        return id;
    }
}
