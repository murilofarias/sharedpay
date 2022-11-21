package br.com.murilofarias.sharedpay.controller;

import br.com.murilofarias.sharedpay.controller.dto.request.AddCreditRequestDto;
import br.com.murilofarias.sharedpay.controller.dto.request.RegisterUserRequestDto;
import br.com.murilofarias.sharedpay.controller.dto.response.UserResponseDto;
import br.com.murilofarias.sharedpay.core.model.User;
import br.com.murilofarias.sharedpay.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody
                                                        RegisterUserRequestDto registerUserRequestDto) {
        User user = registerUserRequestDto.toDomainModel();
        UserResponseDto userResponseDto = new UserResponseDto(userService.registerUser(user));
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/add-credit")
    public ResponseEntity<UserResponseDto> addCredit(
            @PathVariable(value = "id") Long userId,
            @Valid @RequestBody AddCreditRequestDto addCreditRequestDto) {
        UserResponseDto userResponseDto = new UserResponseDto(userService.addUserCredit(userId, addCreditRequestDto.getValue()));
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<User> userEntities = userService.getUsers();
        List<UserResponseDto> usersResponse = userEntities
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(usersResponse, HttpStatus.OK);
    }
}
