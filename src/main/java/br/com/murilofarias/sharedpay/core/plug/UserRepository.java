package br.com.murilofarias.sharedpay.core.plug;

import br.com.murilofarias.sharedpay.core.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByPersonCpf(String cpf);

    boolean existsByPersonCpf(String cpf);
    List<User> findAll();
}
