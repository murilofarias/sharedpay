package br.com.murilofarias.sharedpay.controller.dto.response;

import br.com.murilofarias.sharedpay.core.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {


    private Long id;

    private String firstName;

    private BigDecimal credit;


    private String lastName;

    private String cpf;


    public UserResponseDto(User user){
        this.id = user.getId();
        this.firstName = user.getPerson().getFirstName();
        this.lastName = user.getPerson().getLastName();
        this.cpf = user.getPerson().getCpf();
        this.credit = user.getCredit();
    }
}