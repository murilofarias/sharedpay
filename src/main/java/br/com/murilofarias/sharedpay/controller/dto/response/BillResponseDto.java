package br.com.murilofarias.sharedpay.controller.dto.response;

import br.com.murilofarias.sharedpay.controller.dto.IndividualSpendingDto;
import br.com.murilofarias.sharedpay.controller.dto.PersonDto;
import br.com.murilofarias.sharedpay.core.model.Bill;
import br.com.murilofarias.sharedpay.core.model.IndividualSpending;
import br.com.murilofarias.sharedpay.core.model.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillResponseDto {

    private Long id;


    private BigDecimal additionals;

    private BigDecimal discounts;

    private Boolean hasWaiterService;

    private List<IndividualSpendingDto> individualSpendings;

    private List<PaymentResponseDto> payments;

    private PersonDto owner;

    public BillResponseDto(Bill bill){
        this.id = bill.getId();
        this.additionals = bill.getAdditionals();
        this.discounts = bill.getDiscounts();
        this.hasWaiterService = bill.getHasWaiterService();
        this.individualSpendings = bill.getIndividualSpendings()
                .stream()
                .map(IndividualSpendingDto::new)
                .collect(Collectors.toList());

        this.payments = bill.getPayments()
                .stream()
                .map(PaymentResponseDto::new)
                .collect(Collectors.toList());

        this.owner = new PersonDto(bill.getOwner());

    }
}
