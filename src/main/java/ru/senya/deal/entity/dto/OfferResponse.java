package ru.senya.deal.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OfferResponse {

    List<LoanOfferDTO> offerDTOList;

}
