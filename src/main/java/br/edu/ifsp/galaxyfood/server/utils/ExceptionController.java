package br.edu.ifsp.galaxyfood.server.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExceptionController extends RuntimeException {
    private int status;

    public ExceptionController(int status, String message) {
        super(message);
        this.status = status;
    }
}