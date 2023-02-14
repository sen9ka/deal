package ru.senya.deal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senya.deal.clients.ConveyorClient;
import ru.senya.deal.controllers.exceptionHandler.exceptions.ApplicationNotFoundException;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.ApplicationStatus;
import ru.senya.deal.entity.enums.ChangeType;
import ru.senya.deal.entity.fields.Employment;
import ru.senya.deal.entity.fields.Passport;
import ru.senya.deal.entity.fields.StatusHistory;
import ru.senya.deal.entity.models.Application;
import ru.senya.deal.entity.models.Client;
import ru.senya.deal.entity.models.Credit;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.repositories.ClientRepository;
import ru.senya.deal.controllers.exceptionHandler.exceptions.StatusHistoryProcessingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final ConveyorClient conveyorClient;

    @Transactional
    public List<LoanOfferDTO> makePostRequest(LoanApplicationRequestDTO loanApplicationRequestDTO, String applicationsUrl) {

        Client createdClient = createAndSaveClient(loanApplicationRequestDTO);
        Application createdApplication = createAndSaveApplication(createdClient);
        List<LoanOfferDTO> loanOfferDTOList = conveyorClient.getLoanOffers(loanApplicationRequestDTO, applicationsUrl);
        enrichLoanOffers(loanOfferDTOList, createdApplication);
        return loanOfferDTOList;

    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Application findApplication(Long applicationId) {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(applicationId);

        return optionalApplication.orElseThrow(() -> new ApplicationNotFoundException("Заявка с ID " + applicationId + " не найдена"));
    }

    private Client createAndSaveClient(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = Client.builder()
                .lastName(loanApplicationRequestDTO.getLastName())
                .firstName(loanApplicationRequestDTO.getFirstName())
                .middleName(loanApplicationRequestDTO.getMiddleName())
                .birthDate(loanApplicationRequestDTO.getBirthdate())
                .email(loanApplicationRequestDTO.getEmail())
                .passportId(Passport.builder()
                        .series(loanApplicationRequestDTO.getPassportSeries())
                        .number(loanApplicationRequestDTO.getPassportNumber())
                        .build())
                .employmentId(new Employment())
                .dependentAmount(loanApplicationRequestDTO.getAmount())
                .build();

        clientRepository.save(client);

        return client;
    }

    private Application createAndSaveApplication(Client client) {
        List<String> statusHistoryList = new ArrayList<>();
        StatusHistory firstStatus = StatusHistory.builder()
                .status("CREATED")
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            statusHistoryList.add(mapper.writeValueAsString(firstStatus));
        } catch (JsonProcessingException e) {
            throw new StatusHistoryProcessingException("Ошибка при маппинге поля StatusHistory");
        }


        Application application = Application.builder()
                .clientId(client)
                .creditId(new Credit())
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .appliedOffer("Not Chosen Yet")
                .signDate(LocalDate.now())
                .sesCode(1)
                .statusHistory(String.valueOf(statusHistoryList))
                .build();

        applicationRepository.save(application);

        return application;
    }



    private void enrichLoanOffers(List<LoanOfferDTO> loanOfferDTOList, Application application) {
        loanOfferDTOList.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));
    }

}
