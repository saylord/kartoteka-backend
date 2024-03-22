package kz.logitex.kartoteka.outgoing;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OutgoingLotDTO {
    private List<OutgoingMinDTO> outgoings;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int totalDocuments;
    private long totalOpened;
    private long totalClosed;
    private long totalExpired;
}
