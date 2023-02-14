package ru.senya.deal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.senya.deal.controller.exceptionHandler.exceptions.LoanOfferProcessingException;
import ru.senya.deal.controller.exceptionHandler.exceptions.StatusHistoryProcessingException;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.ChangeType;
import ru.senya.deal.entity.field.StatusHistory;
import ru.senya.deal.entity.model.Application;
import ru.senya.deal.repository.ApplicationRepository;
import ru.senya.deal.controller.exceptionHandler.exceptions.ApplicationNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final ApplicationRepository applicationRepository;

    public Application enrichApplication(LoanOfferDTO loanOfferDTO) {
        Optional<Application> optionalApplication = applicationRepository.findById(loanOfferDTO.getApplicationId());
        
        Application application = optionalApplication.orElseThrow(() -> new ApplicationNotFoundException("Заявка с ID" + loanOfferDTO.getApplicationId() + "не найдена"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<StatusHistory> statusHistoryList;

        try {
            statusHistoryList = mapper.readValue(application.getStatusHistory(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new StatusHistoryProcessingException(Constant.STATUS_HISTORY_ERROR_MESSAGE);
        }

        StatusHistory updatedStatus = StatusHistory.builder()
                .status("UPDATED")
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        statusHistoryList.add(updatedStatus);

        application.setStatusHistory(String.valueOf(statusHistoryList));

        try {
            application.setAppliedOffer(mapper.writeValueAsString(loanOfferDTO));
        } catch (JsonProcessingException e) {
            throw new LoanOfferProcessingException(Constant.LOAN_OFFER_ERROR_MESSAGE + "\n" + loanOfferDTO);
        }

        applicationRepository.save(application);

        return application;

    }
}
