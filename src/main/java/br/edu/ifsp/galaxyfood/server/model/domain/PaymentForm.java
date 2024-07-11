package br.edu.ifsp.galaxyfood.server.model.domain;

import lombok.Getter;

@Getter
public enum PaymentForm {
    DINHEIRO(0),
    CREDITO(1),
    DEBITO(2),
    PIX(3);

    private final int code;
    PaymentForm(int code) {
        this.code = code;
    }

    public static PaymentForm getFromCode(int code) {
        for (PaymentForm paymentForm : PaymentForm.values()) {
            if (paymentForm.code == code) return paymentForm;
        }
        return null;
    }
}
