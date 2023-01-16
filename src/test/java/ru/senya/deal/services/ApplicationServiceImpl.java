package ru.senya.deal.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.ApplicationStatus;
import ru.senya.deal.entity.enums.ChangeType;
import ru.senya.deal.entity.fields.Employment;
import ru.senya.deal.entity.fields.Passport;
import ru.senya.deal.entity.fields.StatusHistory;
import ru.senya.deal.entity.models.Application;
import ru.senya.deal.entity.models.Client;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.repositories.ClientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;

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
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        StatusHistory firstStatus = StatusHistory.builder()
                .status("CREATED")
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        statusHistoryList.add(firstStatus);

        Application application = Application.builder()
                .clientId(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .appliedOffer("Not Chosen Yet")
                .signDate(LocalDate.now())
                .sesCode(1)
                .statusHistory(statusHistoryList.toString())
                .build();

        applicationRepository.save(application);

        return application;
    }

    private void enrichLoanOffers(List<LoanOfferDTO> loanOfferDTOList, Application application) {
        loanOfferDTOList.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));
    }

    private List<LoanOfferDTO> createPostRequest(LoanApplicationRequestDTO loanApplicationRequestDTO, String applicationsUrl, Application createdApplication) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoanApplicationRequestDTO> request = new HttpEntity<>(loanApplicationRequestDTO, headers);
        ResponseEntity<List<LoanOfferDTO>> responseEntity = restTemplate.exchange(applicationsUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});

        List<LoanOfferDTO> loanOfferDTOList = responseEntity.getBody();
        assert loanOfferDTOList != null;
        enrichLoanOffers(loanOfferDTOList, createdApplication);

        return loanOfferDTOList;
    }

    @Transactional
    public List<LoanOfferDTO> makePostRequest(LoanApplicationRequestDTO loanApplicationRequestDTO, String applicationsUrl) {

        Client createdClient = createAndSaveClient(loanApplicationRequestDTO);
        Application createdApplication = createAndSaveApplication(createdClient);

        return createPostRequest(loanApplicationRequestDTO, applicationsUrl, createdApplication);

    }



}