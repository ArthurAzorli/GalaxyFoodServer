package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutComboItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
public class ComboItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Food item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buy_id", nullable = false)
    private Combo combo;

    public ComboItem(UUID id, int quantity, Food item, Combo combo) {
        this.id = id;
        this.quantity = quantity;
        this.item = item;
        this.combo = combo;
    }

    public ComboItem(int quantity, Food item, Combo combo) {
        this.id = UUID.randomUUID();
        this.quantity = quantity;
        this.item = item;
        this.combo = combo;
    }

    public ComboItem(){}

    public OutComboItemDTO toDTO(){
        return new OutComboItemDTO(id, quantity, combo.getId(), item.foodToDTO());
    }
}
