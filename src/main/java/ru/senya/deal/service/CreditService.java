package ru.senya.deal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.senya.deal.controller.exceptionHandler.exceptions.PaymentScheduleProcessingException;
import ru.senya.deal.entity.dto.CreditDTO;
import ru.senya.deal.entity.enums.CreditStatus;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.entity.model.Credit;
import ru.senya.deal.repository.CreditRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final ApplicationService applicationService;
    private final KafkaService kafkaService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Credit sendDocumentsOrDeniedOffer(CreditDTO creditDTO, Long applicationId) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Application application = applicationService.findApplication(applicationId);

        String paymentSchedule;
        try {
            paymentSchedule = mapper.writeValueAsString(creditDTO.getPaymentSchedule());
        } catch (JsonProcessingException e) {
            throw new PaymentScheduleProcessingException(Constant.PAYMENT_SCHEDULE_ERROR_MESSAGE);
        }

        Credit credit = application.getCreditId();
        Credit updatedCredit = credit.toBuilder()
                .amount(creditDTO.getAmount())
                .term(creditDTO.getTerm())
                .monthlyPayment(creditDTO.getMonthlyPayment())
                .rate(creditDTO.getRate())
                .psk(creditDTO.getPsk())
                .paymentSchedule(paymentSchedule)
                .insuranceEnable(creditDTO.getIsInsuranceEnabled())
                .salaryClient(creditDTO.getIsSalaryClient())
                .creditStatus(String.valueOf(CreditStatus.CALCULATED))
                .build();

        if (updatedCredit.getRate().equals(BigDecimal.valueOf(-1))) {
            kafkaTemplate.send("application-denied", kafkaService.sendApplicationDenied(applicationId));
        } else {
            saveCredit(updatedCredit);
            kafkaTemplate.send("create-documents", kafkaService.createDocuments(applicationId));
        }

        return updatedCredit;
    }

    private void saveCredit(Credit credit) {
        creditRepository.save(credit);
    }

}
