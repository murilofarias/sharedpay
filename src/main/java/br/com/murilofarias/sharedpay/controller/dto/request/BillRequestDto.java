package br.com.murilofarias.sharedpay.controller.dto.request;

import br.com.murilofarias.sharedpay.controller.dto.IndividualSpendingDto;
import br.com.murilofarias.sharedpay.controller.dto.PersonDto;
import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.IndividualSpending;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BillRequestDto {

    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 8, fraction=2)
    @NotNull
    private BigDecimal additionals;

    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 8, fraction=2)
    @NotNull
    private BigDecimal discounts;

    @NotNull
    private Boolean hasWaiterService;

    @NotEmpty
    @Size(min=2)
    private List<@Valid IndividualSpendingDto> individualSpendings;

    @NotNull
    private PersonDto owner;

    public Bill toDomainModel(){
        List<IndividualSpending> individualSpendings = this.individualSpendings
                .stream()
                .map(IndividualSpendingDto::toDomainModel)
                .collect(Collectors.toList());

        Bill bill =  new Bill(additionals, discounts, hasWaiterService, individualSpendings, owner.toDomainModel());

        return bill;
    }
}
