package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.PhoneDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "restaurant_phone")
public class RestaurantPhone implements Serializable {
    @Id
    @Column(nullable = false)
    private String phone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public RestaurantPhone(String phone, Restaurant restaurant) {
        this.phone = phone;
        this.restaurant = restaurant;
    }

    public RestaurantPhone() {}

    public PhoneDTO toDTO(){
        return new PhoneDTO(phone);
    }
}