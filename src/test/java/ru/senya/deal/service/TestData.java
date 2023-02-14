package ru.senya.deal.service;

import ru.senya.deal.entity.dto.EmploymentDTO;
import ru.senya.deal.entity.dto.FinishRegistrationRequestDTO;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.Gender;
import ru.senya.deal.entity.enums.MaritalStatus;
import ru.senya.deal.entity.field.Employment;
import ru.senya.deal.entity.field.Passport;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.entity.model.Client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.senya.deal.service.OfferServiceTest.APPLICATION_ID;

public class TestData {

    public static LoanApplicationRequestDTO getTestLoanApplicationRequestDTO() {

        return LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("name")
                .lastName("name")
                .middleName("name")
                .email("email@mail.com")
                .birthdate(LocalDate.of(1999, 3, 22))
                .passportSeries("124514")
                .passportNumber("125151")
                .build();

    }

    public static List<LoanOfferDTO> getTestLoanOfferDTOList() {

        LoanOfferDTO firstOffer = new LoanOfferDTO();
        LoanOfferDTO secondOffer = new LoanOfferDTO();
        LoanOfferDTO thirdOffer = new LoanOfferDTO();
        LoanOfferDTO fourthOffer = new LoanOfferDTO();
        List<LoanOfferDTO> expectedList = new ArrayList<>();
        expectedList.add(firstOffer);
        expectedList.add(secondOffer);
        expectedList.add(thirdOffer);
        expectedList.add(fourthOffer);

        return expectedList;
    }

    public static FinishRegistrationRequestDTO getTestFinishRegistrationRequestDTO() {

        return FinishRegistrationRequestDTO.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .account("account")
                .build();

    }

    public static FinishRegistrationRequestDTO getTestFinishRegistrationRequestDTOWithEmployment() {
        EmploymentDTO employmentDTO = EmploymentDTO.builder().build();

        return FinishRegistrationRequestDTO.builder()
                .employment(employmentDTO)
                .build();
    }

    public static Application getTestApplication() {
        Passport passport = Passport.builder()
                .passportId(1)
                .build();
        Employment employment = Employment.builder()
                .employmentId(1)
                .build();
        Client client = Client.builder()
                .passportId(passport)
                .clientId(1L)
                .employmentId(employment)
                .build();
        return Application.builder()
                .clientId(client)
                .appliedOffer("WrongList")
                .build();
    }

    public static Application getTestApplicationWithWrongStatusHistory() {

        return Application.builder()
                .applicationId(APPLICATION_ID)
                .statusHistory("WrongString")
                .build();

    }

    public static LoanOfferDTO getTestLoanOffer() {

        return LoanOfferDTO.builder()
                .applicationId(APPLICATION_ID)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(119600.0))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9966.7))
                .rate(BigDecimal.valueOf(9.6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
    }

    public static Application getTestApplicationWithStatusHistory() {

        return Application.builder()
                .applicationId(APPLICATION_ID)
                .statusHistory("[{\"status\":\"CREATED\",\"time\":[2023,1,28,17,13,42,664032400],\"changeType\":\"AUTOMATIC\"}]")
                .build();
    }
}
