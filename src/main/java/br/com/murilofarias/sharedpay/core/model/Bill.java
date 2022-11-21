package br.com.murilofarias.sharedpay.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

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

    @Embedded
    private Person owner;

    public Bill(BigDecimal additionals, BigDecimal discounts,
                Boolean hasWaiterService, List<IndividualSpending> individualSpendings, Person owner){

        this.additionals = additionals;
        this.discounts = discounts;
        this.hasWaiterService = hasWaiterService;
        this.individualSpendings = individualSpendings;
        this.owner = owner;

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

        this.payments = individualSpendings
                .stream()
                .filter(individualSpending -> !individualSpending.getPerson().getCpf().equals(owner.getCpf()))
                .map(individualSpending -> {
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
