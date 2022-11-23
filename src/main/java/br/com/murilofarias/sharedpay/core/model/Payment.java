package br.com.murilofarias.sharedpay.core.model;

import br.com.murilofarias.sharedpay.core.error.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static br.com.murilofarias.sharedpay.core.model.PaymentStatus.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column
    private PaymentStatus status;

    @Embedded
    private Person debtor;

    @Column
    private OffsetDateTime fulfillmentDate;

    @Column
    private BigDecimal value;

    @Column
    private String paymentUrl;

    @ManyToOne
    private Bill bill;

    public Payment(Person debtor, BigDecimal value, Bill bill){
        this.debtor = debtor;
        this.value = value;
        this.bill = bill;
        this.status = NON_REQUESTED;
    }

    /**
     * Updates the payment url with the new one and sets the status of the payment to PENDING.
     *
     * @param  paymentUrl   New payment url that will be set to the payment.
     *                      It will be the link the debtor will use to pay
     *
     * @throws DomainException if Payment status is equal to FULFILLED or paymentUrl is blank.
     */
    public void updatePaymentUrl(String paymentUrl){
        if(this.status.equals(FULFILLED)){
            throw new DomainException("Error updating paymentUrl in payment", "Payment has already been made");
        }

        if(paymentUrl == null || paymentUrl.trim().isEmpty())
            throw new DomainException("Error updating paymentUrl in payment", "PaymentUrl can't be blank when status is pending");
        else {
            this.status = PENDING;
            this.paymentUrl = paymentUrl;
        }

    }

    public void setStatus(PaymentStatus status){
        if(status.equals(FULFILLED))
            this.fulfillmentDate = OffsetDateTime.now();

        this.status = status;
    }
}
