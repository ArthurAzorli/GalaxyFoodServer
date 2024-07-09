package br.edu.ifsp.galaxyfood.server.model.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CANCELED(0),
    WAITING(1),
    MAKING(2),
    MADE(3),
    DELIVERED(4);

    private final int code;
    OrderStatus(int code){this.code = code;}

    public static OrderStatus getOrderStatus(int code) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.code == code) return orderStatus;
        }
        return null;
    }
}
