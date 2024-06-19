package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyItemDTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
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

    @Column(nullable = false, columnDefinition = "DATETIME")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

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

    public Buy(UUID id, PaymentForm paymentForm, LocalDateTime date, Address sentAddress, Client client, Restaurant restaurant, List<BuyItem> items) {
        this.id = id;
        this.paymentForm = paymentForm;
        this.date = date;
        this.sentAddress = sentAddress;
        this.client = client;
        this.restaurant = restaurant;
        this.items = items;
    }

    public Buy(PaymentForm paymentForm, LocalDateTime date, Address sentAddress,Client client, Restaurant restaurant, List<BuyItem> items) {
        this.id = UUID.randomUUID();
        this.paymentForm = paymentForm;
        this.date = date;
        this.sentAddress = sentAddress;
        this.client = client;
        this.restaurant = restaurant;
        this.items = items;
    }

    public Buy(PaymentForm paymentForm, LocalDateTime date, Address sentAddress, Client client, Restaurant restaurant) {
        this.id = UUID.randomUUID();
        this.paymentForm = paymentForm;
        this.date = date;
        this.sentAddress = sentAddress;
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
        return new OutBuyDTO(id, paymentForm.getCode(), date, sentAddress, client.toDTO(), restaurant.toDTO(), list);
    }

}
