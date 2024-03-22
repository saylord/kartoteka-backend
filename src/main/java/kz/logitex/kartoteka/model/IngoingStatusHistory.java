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
@Table(name = "ingoing_status_history")
public class IngoingStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ingoingId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_from")
    private Status statusFrom;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_to")
    private Status statusTo;
    @Column(name = "timestamp")
    private Long timestamp;
    @ManyToOne
    private User user;
}
