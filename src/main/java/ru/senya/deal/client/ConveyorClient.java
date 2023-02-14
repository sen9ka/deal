package ru.senya.deal.client;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.senya.deal.config.RestTemplateConfig;
import ru.senya.deal.entity.dto.CreditDTO;
import ru.senya.deal.entity.dto.LoanApplicationRequestDTO;
import ru.senya.deal.entity.dto.LoanOfferDTO;
import ru.senya.deal.entity.dto.ScoringDataDTO;

import java.util.List;

@AllArgsConstructor
@Configuration
public class ConveyorClient {

    RestTemplateConfig restTemplateConfig;

    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO, String applicationsUrl) {
        RestTemplate restTemplate = restTemplateConfig.getRestTemplate();
        HttpHeaders headers = restTemplateConfig.getHeaders();

        HttpEntity<LoanApplicationRequestDTO> request = new HttpEntity<>(loanApplicationRequestDTO, headers);
        ResponseEntity<List<LoanOfferDTO>> responseEntity = restTemplate.exchange(applicationsUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});

        return responseEntity.getBody();
    }

    public CreditDTO sendScoringDataDTO(ScoringDataDTO scoringDataDTO, String calculationsUrl) {
        RestTemplate restTemplate = restTemplateConfig.getRestTemplate();
        HttpHeaders headers = restTemplateConfig.getHeaders();

        HttpEntity<ScoringDataDTO> request = new HttpEntity<>(scoringDataDTO, headers);
        ResponseEntity<CreditDTO> responseEntity = restTemplate.exchange(calculationsUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});

        return responseEntity.getBody();
    }

}
