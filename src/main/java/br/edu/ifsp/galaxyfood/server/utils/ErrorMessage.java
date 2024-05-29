package br.edu.ifsp.galaxyfood.server.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
