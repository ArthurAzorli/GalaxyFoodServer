package br.edu.ifsp.galaxyfood.server.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "client_phone")
public class ClientPhone implements Serializable {
    @Id
    @Column(nullable = false)
    private String phone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false, referencedColumnName = "id")
    private Client client;

    public ClientPhone(String phone, Client client) {
        this.phone = phone;
        this.client = client;
        client.addPhone(this);
    }

    public ClientPhone() {}
}
