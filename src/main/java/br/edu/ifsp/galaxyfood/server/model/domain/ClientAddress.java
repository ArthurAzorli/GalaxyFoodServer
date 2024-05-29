package br.edu.ifsp.galaxyfood.server.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
public class ClientAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Client client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Address address;

    public ClientAddress(UUID id, Client client, Address address) {
        this.id = id;
        this.client = client;
        this.address = address;
    }

    public ClientAddress(Client client, Address address) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.address = address;
        client.addAddress(this);
    }

    public ClientAddress() {
    }
}
