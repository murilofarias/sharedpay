package br.com.murilofarias.sharedpay.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
public class IndividualSpending{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;

    @Embedded
    private Person person;

    @ManyToOne
    @Setter
    private Bill bill;

    public IndividualSpending(BigDecimal value, Person person){
        this.value = value;
        this.person = person;
    }
}
