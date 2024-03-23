package kz.logitex.kartoteka.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "outgoing")
public class Outgoing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentNumber;
    private String description;
    private String exemplar;
    private Long createdTimestamp;
    private Long estimatedTimestamp;
    private Long closedTimestamp;
    private Long documentTimestamp;
    private Long sendingTimestamp;
    @ManyToOne
    private User userLastUpdated;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private Building building;
    @ManyToOne
    private Secret secret;
    @ManyToOne
    private User executor;
    private int copyNumber;
    private int copySheet;
    private int sheet;
    private int schedule;
    private String docDepartmentIndex;
    private int docCopySheet;
    private int docCopyPrint;
    private boolean reregistration;
    private boolean returnAddress;
    private boolean onlyAddress;
}
