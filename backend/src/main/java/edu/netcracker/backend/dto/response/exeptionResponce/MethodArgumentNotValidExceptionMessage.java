package edu.netcracker.backend.dto.response.exeptionResponce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MethodArgumentNotValidExceptionMessage {

    private String field;
    private String message;

}