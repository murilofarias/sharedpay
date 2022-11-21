package br.com.murilofarias.sharedpay.core.service;

import br.com.murilofarias.sharedpay.core.error.DomainException;
import br.com.murilofarias.sharedpay.core.error.ResourceNotFoundException;
import br.com.murilofarias.sharedpay.core.model.User;
import br.com.murilofarias.sharedpay.core.plug.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static br.com.murilofarias.sharedpay.util.CpfUtils.eliminateDotsAndDashes;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user){
        if(userRepository.existsByPersonCpf(eliminateDotsAndDashes(user.getPerson().getCpf())))
            throw new DomainException("Error registering user", "There is already an user with cpf " + user.getPerson().getCpf());

        return userRepository.save(user);
    }

    public User addUserCredit(Long userId, BigDecimal valueToAdd){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("There is no User with cpf " + userId ));
        user.addCredit(valueToAdd);
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
