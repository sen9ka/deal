package ru.senya.deal.entity.fields;

import jakarta.persistence.*;
import lombok.*;
import ru.senya.deal.entity.enums.EmploymentPosition;
import ru.senya.deal.entity.enums.EmploymentStatus;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employment")
public class Employment {

    @Id
    @Column(name = "employment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employmentId;

    @Column(name = "status")
    private EmploymentStatus status;

    @Column(name = "employer_inn")
    private String employerInn;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "position")
    private EmploymentPosition position;

    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;

    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;

}
