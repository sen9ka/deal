package ru.senya.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.senya.deal.entity.dto.CreditDTO;
import ru.senya.deal.entity.dto.FinishRegistrationRequestDTO;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.service.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
@Tag(name = "микросервис Сделка (deal)")
public class DealController {

    @Value("${applicationLink}")
    private String applicationsUrl;

    @Value("${calculationLink}")
    private String calculationsUrl;

    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculationService calculationService;
    private final KafkaService kafkaService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CreditService creditService;

    Logger logger = LoggerFactory.getLogger(DealController.class);

    @PostMapping("/application")
    @Operation(summary = "Прескоринг - 4 кредитных предложения - на основании LoanApplicationRequestDTO")
    public ResponseEntity<Object> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        logger.trace("Application API accessed");
        List<LoanOfferDTO> loanOfferDTOList = applicationService.makePostRequest(loanApplicationRequestDTO, applicationsUrl);
        return new ResponseEntity<>(loanOfferDTOList, HttpStatus.OK);
    }

    @PostMapping("/offer")
    @Operation(summary = "Обновление статуса заявки и истории заказов, установка applied Offer")
    public ResponseEntity<Object> chooseLoanOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        logger.trace("Offer API accessed");
        Application application = offerService.enrichApplication(loanOfferDTO);
        kafkaTemplate.send("finish-registration", kafkaService.completeClearance(loanOfferDTO));
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @PostMapping("/calculate/{applicationId}")
    @Operation(summary = "Скоринг данных, высчитывание ставки(rate), полной стоимости кредита(psk), размера ежемесячного платежа(monthlyPayment), графика ежемесячных платежей")
    public ResponseEntity<Object> enrichScoringDataDTO(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        logger.trace("Calculation API accessed");
        CreditDTO creditDTO = calculationService.makePostRequest(finishRegistrationRequestDTO, applicationId, calculationsUrl);
        creditService.sendDocumentsOrDeniedOffer(creditDTO, applicationId);
        return new ResponseEntity<>(creditDTO, HttpStatus.OK);
    }

}
