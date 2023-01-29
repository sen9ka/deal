package ru.senya.deal.entity.models;

import jakarta.persistence.*;
import ru.senya.deal.entity.enums.CreditStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "credit")
public class Credit {

    @Id
    @Column(name = "credit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Column(name = "payment_schedule")
    private String paymentSchedule;

    @Column(name = "insurance_enable")
    private Boolean insuranceEnable;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    private String creditStatus;

}
