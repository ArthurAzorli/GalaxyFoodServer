package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutScoreDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "DECIMAL(7,1)")
    private BigDecimal score;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Restaurant restaurant;

    public Score(UUID id, BigDecimal score, Client client, Restaurant restaurant) {
        this.id = id;
        this.score = score;
        this.client = client;
        this.restaurant = restaurant;
    }

    public Score(BigDecimal score, Client client, Restaurant restaurant) {
        this.id = UUID.randomUUID();
        this.score = score;
        this.client = client;
        this.restaurant = restaurant;
    }

    public Score() {
    }

    public OutScoreDTO toDTO(){
        return new OutScoreDTO(id, score, client.getId(), restaurant.getId());
    }
}
