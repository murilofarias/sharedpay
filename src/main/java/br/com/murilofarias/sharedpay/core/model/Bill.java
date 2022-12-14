package br.com.murilofarias.sharedpay.core.model;

import br.com.murilofarias.sharedpay.core.error.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.murilofarias.sharedpay.util.CpfUtils.eliminateDotsAndDashes;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bill{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal additionals;

    @Column
    private BigDecimal discounts;

    @Column
    private Boolean hasWaiterService;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<IndividualSpending> individualSpendings;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Column
    private Boolean includeOwnerPayment;

    @Embedded
    private Person owner;

    public Bill(BigDecimal additionals, BigDecimal discounts,
                Boolean hasWaiterService, List<IndividualSpending> individualSpendings,
                Boolean includeOwnerPayment, String ownerCpf){

        this.additionals = additionals;
        this.discounts = discounts;
        this.hasWaiterService = hasWaiterService;
        this.individualSpendings = individualSpendings;
        this.includeOwnerPayment = includeOwnerPayment;



        this.owner = individualSpendings
                .stream()
                .map(IndividualSpending::getPerson)
                .filter(person -> person.getCpf().equals(eliminateDotsAndDashes(ownerCpf)))
                .findAny()
                .orElseThrow(() ->
                        new DomainException("Error creating Bill", "Owner needs to be a person in IndividualSpendings!"));

        individualSpendings
                .stream()
                .forEach(individualSpending -> individualSpending.setBill(this));

    }


    /**
     * it calculates and sets payments for a bill. Each payment is equal to the respective individual spending
     * multiplied by the ratio of the subtotal spending divided by the sum of individual spendings.
     */
    public void generatePayments(){
        BigDecimal individualSpendingsSum = getIndividualSpendingSum();
        BigDecimal total = getBillTotal();

        BigDecimal constantFactor = total.divide(individualSpendingsSum, 6, RoundingMode.HALF_UP);

        Stream<IndividualSpending> individualSpendingsStream  = individualSpendings
                .stream();

        if(!includeOwnerPayment) {
            individualSpendingsStream = individualSpendingsStream.filter(individualSpending -> !individualSpending.getPerson().getCpf().equals(owner.getCpf()));
        }

        this.payments =  individualSpendingsStream.map(individualSpending -> {
                    BigDecimal paymentValue = individualSpending.getValue()
                            .multiply(constantFactor)
                            .setScale(2, RoundingMode.HALF_UP);

                    return new Payment(individualSpending.getPerson(), paymentValue, this);
                })
                .collect(Collectors.toList());

    }

    public BigDecimal getIndividualSpendingSum(){
        BigDecimal individualSpendingsSum= individualSpendings
                .stream()
                .map(IndividualSpending::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return individualSpendingsSum;
    }

    public BigDecimal getBillTotal(){
        BigDecimal individualSpendingsSum= individualSpendings
                .stream()
                .map(IndividualSpending::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWithoutService= individualSpendingsSum.add(additionals)
                .subtract(discounts);

        BigDecimal total = hasWaiterService ?
                totalWithoutService.multiply(new BigDecimal("1.1")) : totalWithoutService;

        return total;
    }

}
