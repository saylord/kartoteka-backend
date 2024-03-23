package kz.logitex.kartoteka.report;

import kz.logitex.kartoteka.outgoing.OutgoingDTO;
import kz.logitex.kartoteka.repository.IngoingRepository;
import kz.logitex.kartoteka.repository.OutgoingRepository;
import kz.logitex.kartoteka.ingoing.IngoingDTO;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final IngoingRepository ingoingRepository;
    private final OutgoingRepository outgoingRepository;

    public ReportResponseDTO getReportIngoing(ReportRequestDTO request) {
        var ingoings = ingoingRepository.findAllByFilters(
                request.getStart(),
                request.getEnd(),
                request.getDescription(),
                request.getBuilding()
        );

        // Собираем карту счетчиков для различных параметров
        Map<String, Long> secretCounts = ingoings.stream()
                .filter(ingoing -> ingoing.getSecret() != null)
                .collect(Collectors.groupingBy(ingoing -> ingoing.getSecret().getName(), Collectors.counting()));

        long totalSchedule = ingoings.stream().mapToLong(IngoingDTO::getSchedule).sum();
        long totalReregistration = ingoings.stream().filter(IngoingDTO::isReregistration).count();

        // Считаем общее количество зарегистрированных
        long totalRegistered = ingoings.size();

        return ReportResponseDTO.builder()
                .start(StringModifier.timestampToDate(request.getStart()))
                .end(StringModifier.timestampToDate(request.getEnd()))
                .totalRegistered(totalRegistered)
                .totalSchedule(totalSchedule)
                .secretOV(secretCounts.getOrDefault("ОВ", 0L))
                .secretC(secretCounts.getOrDefault("С", 0L))
                .secretCC(secretCounts.getOrDefault("СС", 0L))
                .secretCM(secretCounts.getOrDefault("СМ", 0L))
                .secretCCM(secretCounts.getOrDefault("ССМ", 0L))
                .reregistration(totalReregistration)
                .returnAddress(0L)
                .onlyAddress(0L)
                .build();
    }

    public ReportResponseDTO getReportOutgoing(ReportRequestDTO request) {
        var outgoings = outgoingRepository.findAllByFilters(
                request.getStart(),
                request.getEnd(),
                request.getDescription(),
                request.getBuilding(),
                request.getExecutor()
        );

        // Собираем карту счетчиков для различных параметров
        Map<String, Long> secretCounts = outgoings.stream()
                .filter(outgoing -> outgoing.getSecret() != null)
                .collect(Collectors.groupingBy(outgoing -> outgoing.getSecret().getName(), Collectors.counting()));

        long totalSchedule = outgoings.stream().mapToLong(OutgoingDTO::getSchedule).sum();
        long totalReregistration = outgoings.stream().filter(OutgoingDTO::isReregistration).count();
        long totalReturnAddress = outgoings.stream().filter(OutgoingDTO::isReturnAddress).count();
        long totalOnlyAddress = outgoings.stream().filter(OutgoingDTO::isOnlyAddress).count();

        // Считаем общее количество зарегистрированных
        long totalRegistered = outgoings.size();

        return ReportResponseDTO.builder()
                .start(StringModifier.timestampToDate(request.getStart()))
                .end(StringModifier.timestampToDate(request.getEnd()))
                .totalRegistered(totalRegistered)
                .totalSchedule(totalSchedule)
                .secretOV(secretCounts.getOrDefault("ОВ", 0L))
                .secretC(secretCounts.getOrDefault("С", 0L))
                .secretCC(secretCounts.getOrDefault("СС", 0L))
                .secretCM(secretCounts.getOrDefault("СМ", 0L))
                .secretCCM(secretCounts.getOrDefault("ССМ", 0L))
                .reregistration(totalReregistration)
                .returnAddress(totalReturnAddress)
                .onlyAddress(totalOnlyAddress)
                .build();
    }
}
