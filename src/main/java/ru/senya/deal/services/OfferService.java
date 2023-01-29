package ru.senya.deal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.senya.deal.controllers.exceptionHandler.exceptions.LoanOfferProcessingException;
import ru.senya.deal.controllers.exceptionHandler.exceptions.StatusHistoryProcessingException;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.ChangeType;
import ru.senya.deal.entity.fields.StatusHistory;
import ru.senya.deal.entity.models.Application;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.controllers.exceptionHandler.exceptions.ApplicationNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final ApplicationRepository applicationRepository;

    public Application enrichApplication(LoanOfferDTO loanOfferDTO) {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(loanOfferDTO.getApplicationId());
        
        Application application = optionalApplication.orElseThrow(() -> new ApplicationNotFoundException("Заявка с ID" + loanOfferDTO.getApplicationId() + "не найдена"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<StatusHistory> statusHistoryList;

        try {
            statusHistoryList = mapper.readValue(application.getStatusHistory(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new StatusHistoryProcessingException("Ошибка при маппинге поля StatusHistory");
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
            throw new LoanOfferProcessingException("Ошибка при маппинге поля loanOfferDTO" + "\n" + loanOfferDTO);
        }

        applicationRepository.save(application);

        return application;

    }
}
