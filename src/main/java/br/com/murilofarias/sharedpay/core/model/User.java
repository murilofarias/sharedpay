package br.com.murilofarias.sharedpay.core.model;

import br.com.murilofarias.sharedpay.core.error.DomainException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="USER_ACCOUNT")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal credit;

    @Embedded
    private Person person;

    public User(String firstName, String lastName, String cpf){
        this.person = new Person(firstName, lastName, cpf);
        this.credit = BigDecimal.valueOf(0);
    }

    public User(Person person){
        this.person = person;
        this.credit = BigDecimal.valueOf(0);
    }

    public void chargeByBill(Bill bill){
        BigDecimal totalBill = bill.getBillTotal();
        if(totalBill.compareTo(credit) > 0)
            throw new DomainException("Error registering Bill", "User doesn't have enough credit");

        this.credit = credit.subtract(bill.getBillTotal());
    }

    public void addCredit(BigDecimal valueToAdd){
        this.credit = this.credit.add(valueToAdd);
    }
}
