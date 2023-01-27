package ru.senya.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senya.deal.clients.ConveyorClient;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.repositories.ClientRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ConveyorClient conveyorClient;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void makePostRequest() {

    }

}