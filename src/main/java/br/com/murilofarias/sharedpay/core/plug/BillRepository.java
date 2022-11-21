package br.com.murilofarias.sharedpay.core.plug;

import br.com.murilofarias.sharedpay.core.model.Bill;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BillRepository extends PagingAndSortingRepository<Bill, Long>{

}
