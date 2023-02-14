package ru.senya.deal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senya.deal.client.ConveyorClient;
import ru.senya.deal.controller.exceptionHandler.exceptions.ApplicationNotFoundException;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.repository.ApplicationRepository;
import ru.senya.deal.repository.ClientRepository;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    public static final long APPLICATION_ID = 1L;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ConveyorClient conveyorClient;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    @DisplayName("Сравнить ожидаемый List<LoanOfferDTO> с реальным")
    void shouldReturnSameListTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = TestData.getTestLoanApplicationRequestDTO();
        Application application = Application.builder().applicationId(1L).build();
        List<LoanOfferDTO> expectedList = TestData.getTestLoanOfferDTOList();

        when(applicationService.makePostRequest(loanApplicationRequestDTO, any())).thenReturn(expectedList);
        when(applicationRepository.save(any())).thenReturn(application);

        assertEquals(expectedList, applicationService.makePostRequest(loanApplicationRequestDTO, any()));
    }

    @Test
    @DisplayName("Должен вернуть ApplicationNotFoundException")
    void shouldReturnApplicationNotFoundExceptionTest() {
        when(applicationRepository.findById(APPLICATION_ID)).thenReturn(Optional.empty());
        assertThrows(ApplicationNotFoundException.class, () -> applicationService.findApplication(APPLICATION_ID));
    }

    @Test
    @DisplayName("Должен успешно вернуть заявку")
    void findApplicationTest() {
        Application expected = Application.builder()
                .build();
        when(applicationRepository.findById(any())).thenReturn(Optional.ofNullable(expected));
        Application actual = applicationService.findApplication(APPLICATION_ID);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен успешно вернуть заявку")
    void findApplication3() {
        Application expected = mock(Application.class);
        when(applicationRepository.findById(any())).thenReturn(Optional.ofNullable(expected));
        Application actual = applicationService.findApplication(APPLICATION_ID);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен увернуть RuntimeException")
    void shouldReturnRuntimeExceptionTest() {
        when(applicationRepository.findById(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> applicationService.findApplication(APPLICATION_ID));
    }
}