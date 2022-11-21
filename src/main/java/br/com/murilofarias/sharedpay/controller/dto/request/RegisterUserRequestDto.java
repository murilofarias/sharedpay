package br.com.murilofarias.sharedpay.controller.dto.request;

import br.com.murilofarias.sharedpay.core.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterUserRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @CPF
    @NotBlank
    private String cpf;

    public User toDomainModel(){
        return new User(firstName, lastName, cpf);
    }
}

