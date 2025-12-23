package com.codingshuttle.springbootwebtutorial.springbootwebtutorial.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data                  // it is here to use getters and setters
@Builder               //help in building aboject fast insted of using traditional way by using new keyword
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> subErrors;

}
