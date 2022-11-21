package br.com.murilofarias.sharedpay.controller.dto;

import br.com.murilofarias.sharedpay.core.model.IndividualSpending;
import br.com.murilofarias.sharedpay.core.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class IndividualSpendingDto {

    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 8, fraction=2)
    @NotNull
    private BigDecimal value;

    @Valid
    @NotNull
    private PersonDto person;

    public IndividualSpending toDomainModel(){
        return new IndividualSpending(value, person.toDomainModel());
    }

    public IndividualSpendingDto(IndividualSpending individualSpending){
        this.value = individualSpending.getValue();
        this.person = new PersonDto(individualSpending.getPerson());
    }
}
