package br.edu.ifsp.galaxyfood.server.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Address  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR")
    private UUID id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 9)
    private String cep;

    public Address(UUID id, String street, String number, String neighborhood, String city, String state, String cep) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.cep = cep;
    }

    public Address(String street, String number, String neighborhood, String city, String state, String cep) {
        this.id = UUID.randomUUID();
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.cep = cep;
    }

    public Address() {
    }
}
