package br.com.murilofarias.sharedpay.adapter.paymentservice.dto;

import br.com.murilofarias.sharedpay.core.model.Person;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerDto {

    private String firstName;

    private String lastName;

    private String document;

    public BuyerDto(Person person){
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.document = person.getCpf();
    }
}
