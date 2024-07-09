package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyItemDTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Buy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "payment_form", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentForm paymentForm;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false, columnDefinition = "DATETIME")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "DECIMAL(7,2)")
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(nullable = false, columnDefinition = "DECIMAL(7,2)")
    private BigDecimal discount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "sent_address")
    private Address sentAddress;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToMany(mappedBy = "buy", cascade = CascadeType.REMOVE)

    private List<BuyItem> items = new ArrayList<>();

    public Buy(UUID id, PaymentForm paymentForm, OrderStatus orderStatus, LocalDateTime date, BigDecimal deliveryFee, BigDecimal discount, Address sentAddress, Client client, Restaurant restaurant, List<BuyItem> items) {
        this.id = id;
        this.paymentForm = paymentForm;
        this.orderStatus = orderStatus;
        this.date = date;
        this.sentAddress = sentAddress;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.client = client;
        this.restaurant = restaurant;
        this.items = items;
    }

    public Buy(PaymentForm paymentForm, OrderStatus orderStatus, LocalDateTime date, BigDecimal deliveryFee, BigDecimal discount, Address sentAddress,Client client, Restaurant restaurant, List<BuyItem> items) {
        this.paymentForm = paymentForm;
        this.orderStatus = orderStatus;
        this.date = date;
        this.sentAddress = sentAddress;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.client = client;
        this.restaurant = restaurant;
        this.items = items;
    }

    public Buy(PaymentForm paymentForm, OrderStatus orderStatus, LocalDateTime date, BigDecimal deliveryFee, BigDecimal discount, Address sentAddress, Client client, Restaurant restaurant) {
        this.paymentForm = paymentForm;
        this.orderStatus = orderStatus;
        this.date = date;
        this.sentAddress = sentAddress;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.client = client;
        this.restaurant = restaurant;
    }

    public Buy() {}

    public boolean addBuyItem(BuyItem buyItem) {
        if (items.contains(buyItem)) return false;
        return items.add(buyItem);
    }

    public OutBuyDTO toDTO(){
        List<OutBuyItemDTO> list = new ArrayList<>();
        for (var item : items) list.add(item.toDTO());
        return new OutBuyDTO(id, paymentForm.getCode(), orderStatus.getCode(), date, deliveryFee, discount, sentAddress, client.toDTO(), restaurant.toDTO(), list);
    }

}
