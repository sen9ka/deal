package ru.senya.deal.entity.models;

import jakarta.persistence.*;
import lombok.*;
import ru.senya.deal.entity.enums.ApplicationStatus;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application")
public class Application {

    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client clientId;

    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Credit creditId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "applied_offer")
    private String appliedOffer;

    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Column(name = "status_history", columnDefinition = "jsonb")
    @JsonbProperty
    private String statusHistory;

}
