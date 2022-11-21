package br.com.murilofarias.sharedpay.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static br.com.murilofarias.sharedpay.util.CpfUtils.eliminateDotsAndDashes;

@Getter
@Embeddable
@NoArgsConstructor
public class Person {

    private String firstName;

    private String lastName;

    private String cpf;

    public Person(String firstName, String lastName, String cpf){
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = eliminateDotsAndDashes(cpf);
    }

}
