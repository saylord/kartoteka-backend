package kz.logitex.kartoteka.ingoing;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IngoingLotDTO {
    private List<IngoingMinDTO> ingoings;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int totalDocuments;
    private long totalOpened;
    private long totalClosed;
    private long totalExpired;
}
