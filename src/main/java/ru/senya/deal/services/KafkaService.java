package ru.senya.deal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.senya.deal.controllers.exceptionHandler.exceptions.EmailMessageProcessingException;
import ru.senya.deal.entity.dto.EmailMessage;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.enums.Theme;
import ru.senya.deal.entity.models.Application;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final ObjectMapper mapper;
    private final ApplicationService applicationService;

    public String completeClearance(LoanOfferDTO loanOfferDTO) {
        Application application = applicationService.findApplication(loanOfferDTO.getApplicationId());

        try {
            return mapper.writeValueAsString(EmailMessage.builder()
                    .applicationId(application.getApplicationId())
                    .address(application.getClientId().getEmail())
                    .theme(Theme.APPROVED)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EmailMessageProcessingException("Ошибка при парсинге EmailMessage");
        }
    }

    public String createDocuments(Long applicationId) {
        Application application = applicationService.findApplication(applicationId);

        try {
            return mapper.writeValueAsString(EmailMessage.builder()
                    .applicationId(application.getApplicationId())
                    .address(application.getClientId().getEmail())
                    .theme(Theme.CC_APPROVED)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EmailMessageProcessingException("Ошибка при парсинге EmailMessage");
        }
    }

    public String prepareDocuments(Long applicationId) {
        Application application = applicationService.findApplication(applicationId);

        try {
            return mapper.writeValueAsString(EmailMessage.builder()
                    .applicationId(application.getApplicationId())
                    .address(application.getClientId().getEmail())
                    .theme(Theme.PREPARE_DOCUMENTS)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EmailMessageProcessingException("Ошибка при парсинге EmailMessage");
        }
    }

    public String sendDocuments(Long applicationId) {
        Application application = applicationService.findApplication(applicationId);

        try {
            return mapper.writeValueAsString(EmailMessage.builder()
                    .applicationId(application.getApplicationId())
                    .address(application.getClientId().getEmail())
                    .theme(Theme.DOCUMENT_CREATED)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EmailMessageProcessingException("Ошибка при парсинге EmailMessage");
        }
    }

    public String sendApplicationDenied(Long applicationId) {
        Application application = applicationService.findApplication(applicationId);

        try {
            return mapper.writeValueAsString(EmailMessage.builder()
                    .applicationId(application.getApplicationId())
                    .address(application.getClientId().getEmail())
                    .theme(Theme.CC_DENIED)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EmailMessageProcessingException("Ошибка при парсинге EmailMessage");
        }
    }

}
