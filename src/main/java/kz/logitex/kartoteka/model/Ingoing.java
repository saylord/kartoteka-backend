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
    private String description;
    private String resolution;
    private Long createdTimestamp;
    private Long estimatedTimestamp;
    private Long closedTimestamp;
    private Long documentTimestamp;
    @ManyToOne
    private User userLastUpdated;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private Building building;
    @ManyToOne
    private Secret secret;
    private int copyNumber;
    private int copySheet;
    private int sheet;
    private int schedule;
    private boolean reregistration;
}

