package ru.senya.deal.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.dto.ScoringDataDTO;
import ru.senya.deal.entity.models.Application;

import java.util.List;

@AllArgsConstructor
@Configuration
public class RestTemplateMethods {

    RestTemplateConfig restTemplateConfig;

    public List<LoanOfferDTO> createPostRequestToApplication(LoanApplicationRequestDTO loanApplicationRequestDTO, String applicationsUrl, Application createdApplication) {
        RestTemplate restTemplate = restTemplateConfig.getRestTemplate();
        HttpHeaders headers = restTemplateConfig.getHeaders();

        HttpEntity<LoanApplicationRequestDTO> request = new HttpEntity<>(loanApplicationRequestDTO, headers);
        ResponseEntity<List<LoanOfferDTO>> responseEntity = restTemplate.exchange(applicationsUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});

        return responseEntity.getBody();
    }

    public void createPostRequestToCalculate(ScoringDataDTO scoringDataDTO, String calculationsUrl) {
        RestTemplate restTemplate = restTemplateConfig.getRestTemplate();
        HttpHeaders headers = restTemplateConfig.getHeaders();

        HttpEntity<ScoringDataDTO> request = new HttpEntity<>(scoringDataDTO, headers);
        restTemplate.exchange(calculationsUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});
    }

}
