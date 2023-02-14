package ru.senya.deal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senya.deal.client.ConveyorClient;
import ru.senya.deal.controller.exceptionHandler.exceptions.LoanOfferProcessingException;
import ru.senya.deal.entity.dto.FinishRegistrationRequestDTO;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.repository.ClientRepository;
import ru.senya.deal.repository.EmploymentRepository;
import ru.senya.deal.repository.PassportRepository;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    public static final long APPLICATION_ID = 1L;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PassportRepository passportRepository;

    @Mock
    private EmploymentRepository employmentRepository;

    @Mock
    private ConveyorClient conveyorClient;

    @InjectMocks
    private CalculationService calculationService;

    @Test
    @DisplayName("Заявка с указанным ID в базе не найдена")
    void findApplicationShouldReturnRuntimeExceptionTest() {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder().build();
        when(applicationService.findApplication(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> calculationService.makePostRequest(finishRegistrationRequestDTO, APPLICATION_ID, "url"));
    }

    @Test
    @DisplayName("Не удалось сохранить клиента в базу")
    void saveClientShouldReturnRuntimeExceptionTest() {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = TestData.getTestFinishRegistrationRequestDTO();
        assertThrows(RuntimeException.class, () -> calculationService.makePostRequest(finishRegistrationRequestDTO, APPLICATION_ID, "url"));
    }

    @Test
    @DisplayName("Не удалось спарсить поле LoanOfferDTO")
    void shouldThrowLoanOfferProcessingException() {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = TestData.getTestFinishRegistrationRequestDTOWithEmployment();
        Application application = TestData.getTestApplication();

        when(applicationService.findApplication(any())).thenReturn(application);
        assertThrows(LoanOfferProcessingException.class, () -> calculationService.makePostRequest(finishRegistrationRequestDTO, APPLICATION_ID, "url"));
    }



}