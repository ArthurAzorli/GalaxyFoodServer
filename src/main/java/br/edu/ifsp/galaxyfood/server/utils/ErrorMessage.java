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
        this.message = e.getMessage();
        this.status = e.getStatus();
        this.timestamp = LocalDateTime.now();
    }
    public ErrorMessage(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}
