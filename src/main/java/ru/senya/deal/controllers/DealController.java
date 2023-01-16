package ru.senya.deal.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senya.deal.entity.dto.FinishRegistrationRequestDTO;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.services.ApplicationService;
import ru.senya.deal.services.CalculationService;
import ru.senya.deal.services.OfferService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
@Tag(name = "икросервис Сделка (deal)")
public class DealController {

    @Value("${applicationLink}")
    private String applicationsUrl;

    @Value("${calculationLink}")
    private String calculationsUrl;

    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculationService calculationService;

    @PostMapping("/application")
    @Operation(summary = "Прескоринг - 4 кредитных предложения - на основании LoanApplicationRequestDTO")
    public ResponseEntity<?> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        List<LoanOfferDTO> loanOfferDTOList = applicationService.makePostRequest(loanApplicationRequestDTO, applicationsUrl);
        return new ResponseEntity<>(loanOfferDTOList, HttpStatus.OK);
    }

    @PutMapping("/offer")
    @Operation(summary = "Обновление статуса заявки и истории заказов, установка applied Offer")
    public ResponseEntity<?> chooseLoanOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws JsonProcessingException {
        offerService.enrichApplication(loanOfferDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/calculate/{applicationId}")
    @Operation(summary = "Скоринг данных, высчитывание ставки(rate), полной стоимости кредита(psk), размера ежемесячного платежа(monthlyPayment), графика ежемесячных платежей")
    public ResponseEntity<?> enrichScoringDataDTO(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) throws JsonProcessingException {
        calculationService.makePostRequest(finishRegistrationRequestDTO, applicationId, calculationsUrl);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
