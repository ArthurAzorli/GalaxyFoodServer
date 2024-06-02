package br.edu.ifsp.galaxyfood.server.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorMessage implements Serializable {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    final private Boolean result = false;

    public ErrorMessage(ExceptionController e) {
        message = e.getMessage();
        status = e.getStatus();
        timestamp = LocalDateTime.now();
    }
}
