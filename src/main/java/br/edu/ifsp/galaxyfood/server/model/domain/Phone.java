package br.edu.ifsp.galaxyfood.server.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Phone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String phone;

    public Phone(UUID id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public Phone(String phone) {
        this.phone = phone;
    }

    public Phone() {}
}
