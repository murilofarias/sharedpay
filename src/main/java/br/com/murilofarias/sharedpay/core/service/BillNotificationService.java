package br.com.murilofarias.sharedpay.core.service;

import br.com.murilofarias.sharedpay.core.model.Payment;
import br.com.murilofarias.sharedpay.core.model.PaymentStatus;
import br.com.murilofarias.sharedpay.core.plug.PaymentRepository;
import br.com.murilofarias.sharedpay.core.plug.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Long.parseLong;

@Service
public class BillNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(BillNotificationService.class);

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    public String checkPaymentStatus(String paymentId){

        taskScheduler.submit(new Runnable() {
            @Override
            public void run() {                try {
                    PaymentStatus status = paymentService.requestPaymentStatus(paymentId);
                    Payment payment = paymentRepository.findById(parseLong(paymentId)).get();
                    payment.setStatus(status);
                    paymentRepository.save(payment);
                    logger.info("Notification of Payment status of id " + paymentId + " is " + status.toString());

                }catch(Exception e){
                    logger.error("Erro ocorrido na requisição do status do pagamento" + paymentId);
                }

            }
        });

        return "Ok";
    }
}




