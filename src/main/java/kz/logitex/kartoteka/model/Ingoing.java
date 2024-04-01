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
@Table(name = "ingoing")
public class Ingoing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentNumber;
    private String cardNumber;
    private String description;
    private String resolution;
    private Long createdTimestamp;
    private Long estimatedTimestamp;
    private Long closedTimestamp;
    private Long documentTimestamp;
    @ManyToOne
    private User executor;
    @ManyToOne
    private User userLastUpdated;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private Building building;
    @ManyToOne
    private Secret secret;
    private String exemplar;
    private int totalSheet;
    private int sheet;
    private int schedule;
    private Long reregistrationTimestamp;
    private String caseNumber;
    private String nomenclature;
    private Long annualTimestamp;
    private Long semiAnnualTimestamp;
    private Long monthlyTimestamp;
    private Long tenDayTimestamp;
    private boolean annual = false;
    private boolean semiAnnual = false;
    private boolean monthly = false;
    private boolean tenDay = false;
}

