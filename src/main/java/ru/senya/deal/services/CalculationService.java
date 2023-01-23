package ru.senya.deal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.senya.deal.clients.ConveyorClient;
import ru.senya.deal.controllers.exceptionHandler.exceptions.LoanOfferProcessingException;
import ru.senya.deal.entity.dto.FinishRegistrationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.dto.ScoringDataDTO;
import ru.senya.deal.entity.fields.Employment;
import ru.senya.deal.entity.fields.Passport;
import ru.senya.deal.entity.models.Application;
import ru.senya.deal.entity.models.Client;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.repositories.ClientRepository;
import ru.senya.deal.repositories.EmploymentRepository;
import ru.senya.deal.repositories.PassportRepository;
import ru.senya.deal.controllers.exceptionHandler.exceptions.ApplicationNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    private final PassportRepository passportRepository;
    private final EmploymentRepository employmentRepository;
    private final ConveyorClient conveyorClient;

    public void makePostRequest(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId, String calculationsUrl) {
        enrichClient(finishRegistrationRequestDTO, applicationId);
        ScoringDataDTO scoringDataDTO = enrichScoringDataDTO(applicationId);

        conveyorClient.sendScoringDataDTO(scoringDataDTO, calculationsUrl);
    }

    private Application findApplication(Long applicationId) {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(applicationId);

        return optionalApplication.orElseThrow(() -> new ApplicationNotFoundException("Заявка с ID " + applicationId + " не найдена"));
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
                .employerInn(finishRegistrationRequestDTO.getEmployment().getEmployerINN())
                .salary(finishRegistrationRequestDTO.getEmployment().getSalary())
                .position(finishRegistrationRequestDTO.getEmployment().getPosition())
                .workExperienceTotal(finishRegistrationRequestDTO.getEmployment().getWorkExperienceTotal())
                .workExperienceCurrent(finishRegistrationRequestDTO.getEmployment().getWorkExperienceCurrent())
                .build();

        employmentRepository.save(updatedEmployment);
    }

    private void enrichClient(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {

        Application application = findApplication(applicationId);
        Client client = findAndUpdateClient(finishRegistrationRequestDTO, application);
        findAndUpdatePassport(finishRegistrationRequestDTO, client);
        findAndUpdateEmployment(finishRegistrationRequestDTO, client);

    }

    private ScoringDataDTO enrichScoringDataDTO(Long applicationId)  {

        ObjectMapper mapper = new ObjectMapper();

        Application application = findApplication(applicationId);
        LoanOfferDTO loanOfferDTO = null;
        try {
            loanOfferDTO = mapper.readValue(application.getAppliedOffer(), LoanOfferDTO.class);
        } catch (JsonProcessingException e) {
            throw new LoanOfferProcessingException("Ошибка при маппинге loanOfferDTO" + "\n" + loanOfferDTO.toString());
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
                .employment(application.getClientId().getEmploymentId())
                .account(application.getClientId().getAccount())
                .isInsuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDTO.getIsSalaryClient())
                .build();
    }
}
