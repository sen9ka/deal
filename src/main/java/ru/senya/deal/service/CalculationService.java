package ru.senya.deal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.senya.deal.client.ConveyorClient;
import ru.senya.deal.controller.exceptionHandler.exceptions.LoanOfferProcessingException;
import ru.senya.deal.entity.dto.*;
import ru.senya.deal.entity.field.Employment;
import ru.senya.deal.entity.field.Passport;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.entity.model.Client;
import ru.senya.deal.repository.ClientRepository;
import ru.senya.deal.repository.EmploymentRepository;
import ru.senya.deal.repository.PassportRepository;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final ApplicationService applicationService;
    private final ClientRepository clientRepository;
    private final PassportRepository passportRepository;
    private final EmploymentRepository employmentRepository;
    private final ConveyorClient conveyorClient;

    public CreditDTO makePostRequest(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId, String calculationsUrl) {
        ScoringDataDTO scoringDataDTO = createScoringDataDTO(finishRegistrationRequestDTO, applicationId);

        return conveyorClient.sendScoringDataDTO(scoringDataDTO, calculationsUrl);
    }

    private ScoringDataDTO createScoringDataDTO(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        enrichClient(finishRegistrationRequestDTO, applicationId);
        return enrichScoringDataDTO(applicationId);
    }

    private Client findAndUpdateClient(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application) {
        Client client = application.getClientId();
        Client updatedClient = client.toBuilder()
                .gender(finishRegistrationRequestDTO.getGender())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .account(finishRegistrationRequestDTO.getAccount())
                .build();

        clientRepository.save(updatedClient);
        return client;
    }

    private void findAndUpdatePassport(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Client client) {
        Passport passport = client.getPassportId();
        Passport updatedPassport = passport.toBuilder()
                .issueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .issueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .build();

        passportRepository.save(updatedPassport);
    }

    private void findAndUpdateEmployment(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Client client) {
        Employment employment = client.getEmploymentId();
        Employment updatedEmployment = employment.toBuilder()
                .status(finishRegistrationRequestDTO.getEmployment().getStatus())
                .employerINN(finishRegistrationRequestDTO.getEmployment().getEmployerINN())
                .salary(finishRegistrationRequestDTO.getEmployment().getSalary())
                .position(finishRegistrationRequestDTO.getEmployment().getPosition())
                .workExperienceTotal(finishRegistrationRequestDTO.getEmployment().getWorkExperienceTotal())
                .workExperienceCurrent(finishRegistrationRequestDTO.getEmployment().getWorkExperienceCurrent())
                .build();

        employmentRepository.save(updatedEmployment);
    }

    private void enrichClient(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {

        Application application = applicationService.findApplication(applicationId);
        Client client = findAndUpdateClient(finishRegistrationRequestDTO, application);
        findAndUpdatePassport(finishRegistrationRequestDTO, client);
        findAndUpdateEmployment(finishRegistrationRequestDTO, client);

    }

    private EmploymentDTO convertToEmploymentDTO(Employment employment) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(employment, EmploymentDTO.class);
    }

    private ScoringDataDTO enrichScoringDataDTO(Long applicationId)  {

        ObjectMapper mapper = new ObjectMapper();

        Application application = applicationService.findApplication(applicationId);
        LoanOfferDTO loanOfferDTO = null;
        try {
            loanOfferDTO = mapper.readValue(application.getAppliedOffer(), LoanOfferDTO.class);
        } catch (JsonProcessingException e) {
            throw new LoanOfferProcessingException("Ошибка при маппинге loanOfferDTO");
        }

        return ScoringDataDTO.builder()
                .amount(loanOfferDTO.getTotalAmount())
                .term(loanOfferDTO.getTerm())
                .firstName(application.getClientId().getFirstName())
                .lastName(application.getClientId().getLastName())
                .middleName(application.getClientId().getMiddleName())
                .gender(application.getClientId().getGender())
                .birthdate(application.getClientId().getBirthDate())
                .passportSeries(application.getClientId().getPassportId().getSeries())
                .passportNumber(application.getClientId().getPassportId().getNumber())
                .passportIssueDate(application.getClientId().getPassportId().getIssueDate())
                .passportIssueBranch(application.getClientId().getPassportId().getIssueBranch())
                .maritalStatus(application.getClientId().getMaritalStatus())
                .dependentAmount(application.getClientId().getDependentAmount())
                .employment(convertToEmploymentDTO(application.getClientId().getEmploymentId()))
                .account(application.getClientId().getAccount())
                .isInsuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDTO.getIsSalaryClient())
                .build();
    }
}
