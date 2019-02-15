package edu.netcracker.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestExceptionMessage<T> {

    private int code;
    private List<T> messages;
    private String currentTime;
}