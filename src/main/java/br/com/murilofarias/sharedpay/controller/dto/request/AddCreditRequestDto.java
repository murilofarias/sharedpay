package br.com.murilofarias.sharedpay.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class AddCreditRequestDto {

    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 8, fraction=2)
    @NotNull
    private BigDecimal value;
}
