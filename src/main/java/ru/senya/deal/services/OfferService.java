package ru.senya.deal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.ChangeType;
import ru.senya.deal.entity.fields.StatusHistory;
import ru.senya.deal.entity.models.Application;
import ru.senya.deal.repositories.ApplicationRepository;
import ru.senya.deal.util.ApplicationNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final ApplicationRepository applicationRepository;

    public void enrichApplication(LoanOfferDTO loanOfferDTO) throws JsonProcessingException {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(loanOfferDTO.getApplicationId());
        
        Application application = optionalApplication.orElseThrow(ApplicationNotFoundException::new);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<StatusHistory> statusHistoryList = mapper.readValue(application.getStatusHistory(), new TypeReference<>() {});

        StatusHistory updatedStatus = StatusHistory.builder()
                .status("UPDATED")
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        statusHistoryList.add(updatedStatus);

        application.setStatusHistory(String.valueOf(statusHistoryList));
        application.setAppliedOffer(mapper.writeValueAsString(loanOfferDTO));

        applicationRepository.save(application);

    }
}
