package br.com.murilofarias.sharedpay.controller.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
}

