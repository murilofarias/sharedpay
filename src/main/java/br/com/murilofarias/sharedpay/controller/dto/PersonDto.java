package br.com.murilofarias.sharedpay.controller.dto;

import br.com.murilofarias.sharedpay.core.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PersonDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @CPF
    @NotBlank
    private String cpf;

    public Person toDomainModel(){
        return new Person(firstName, lastName, cpf);
    }

    public PersonDto(Person person){
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.cpf = person.getCpf();
    }

}
