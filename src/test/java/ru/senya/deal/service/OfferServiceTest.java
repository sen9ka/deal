package ru.senya.deal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senya.deal.controller.exceptionHandler.exceptions.ApplicationNotFoundException;
import ru.senya.deal.controller.exceptionHandler.exceptions.StatusHistoryProcessingException;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.repository.ApplicationRepository;
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
        when(applicationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ApplicationNotFoundException.class, () -> offerService.enrichApplication(loanOfferDTO));
    }

    @Test
    @DisplayName("Должен вернуть StatusHistoryProcessingException")
    void shouldReturnStatusHistoryProcessingExceptionTest() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        Application testApplication = TestData.getTestApplicationWithWrongStatusHistory();

        when(applicationRepository.findById(any())).thenReturn(Optional.ofNullable(testApplication));
        assertThrows(StatusHistoryProcessingException.class, () -> offerService.enrichApplication(loanOfferDTO));
    }

    @Test
    @DisplayName("В поле StatusHistory добавляются данные")
    void statusHistoryAddsDataTest() {
        LoanOfferDTO loanOffer = TestData.getTestLoanOffer();
        Application application = TestData.getTestApplicationWithStatusHistory();
        when(applicationRepository.findById(any())).thenReturn(Optional.ofNullable(application));
        assertNotEquals(application.getStatusHistory(), offerService.enrichApplication(loanOffer).getStatusHistory());

    }
}