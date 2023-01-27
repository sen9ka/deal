package ru.senya.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senya.deal.clients.ConveyorClient;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.repositories.ClientRepository;
import ru.senya.deal.repositories.EmploymentRepository;
import ru.senya.deal.repositories.PassportRepository;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

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
    void makePostRequest() {



    }

}