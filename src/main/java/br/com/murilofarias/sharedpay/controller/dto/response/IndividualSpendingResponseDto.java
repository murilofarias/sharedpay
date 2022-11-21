package br.com.murilofarias.sharedpay.controller.dto.response;

import br.com.murilofarias.sharedpay.controller.dto.PersonDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndividualSpendingResponseDto {


    private BigDecimal value;

    private PersonDto person;
}
