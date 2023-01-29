package ru.senya.deal.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senya.deal.controllers.exceptionHandler.exceptions.ApplicationNotFoundException;
import ru.senya.deal.controllers.exceptionHandler.exceptions.StatusHistoryProcessingException;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.models.Application;
import ru.senya.deal.repositories.ApplicationRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    public static final long APPLICATION_ID = 1L;
    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private OfferService offerService;

    @Test
    @DisplayName("Должен вернуть ApplicationNotFoundException")
    void shouldReturnApplicationNotFoundExceptionTest() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        when(applicationRepository.findByApplicationId(any())).thenReturn(Optional.empty());
        assertThrows(ApplicationNotFoundException.class, () -> offerService.enrichApplication(loanOfferDTO));
    }

    @Test
    @DisplayName("Должен вернуть StatusHistoryProcessingException")
    void shouldReturnStatusHistoryProcessingExceptionTest() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        Application testApplication = Application.builder()
                .applicationId(APPLICATION_ID)
                .statusHistory("WrongString")
                .build();
        when(applicationRepository.findByApplicationId(any())).thenReturn(Optional.ofNullable(testApplication));
        assertThrows(StatusHistoryProcessingException.class, () -> offerService.enrichApplication(loanOfferDTO));
    }

    @Test
    @DisplayName("В поле StatusHistory добавляются данные")
    void statusHistoryAddsDataTest() {
        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .applicationId(APPLICATION_ID)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(119600.0))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9966.7))
                .rate(BigDecimal.valueOf(9.6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
        Application application = Application.builder()
                .applicationId(APPLICATION_ID)
                .statusHistory("[{\"status\":\"CREATED\",\"time\":[2023,1,28,17,13,42,664032400],\"changeType\":\"AUTOMATIC\"}]")
                .build();
        when(applicationRepository.findByApplicationId(any())).thenReturn(Optional.ofNullable(application));
        assertNotEquals(application.getStatusHistory(), offerService.enrichApplication(loanOffer).getStatusHistory());

    }
}