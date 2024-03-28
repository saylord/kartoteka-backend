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
    private String cardNumber;
    private String description;
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
    private String exemplar;
    private int totalSheet;
    private int sheet;
    private int schedule;
    private String docDepartmentIndex;
    private int docCopyPrint;
    private Long reregistrationTimestamp;
    private String caseNumber;
    private String destraction;
    private String nomenclature;
}
