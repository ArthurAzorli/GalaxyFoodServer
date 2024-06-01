package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "buy_item")
public class BuyItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR")
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private PackageItem item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buy_id", nullable = false)
    private Buy buy;

    public BuyItem(UUID id, Buy buy, PackageItem item, int quantity) {
        this.id = id;
        this.buy = buy;
        this.item = item;
        this.quantity = quantity;
    }

    public BuyItem(Buy buy, PackageItem item, int quantity) {
        this.id = UUID.randomUUID();
        this.buy = buy;
        this.item = item;
        this.quantity = quantity;
    }

    public BuyItem() {}

    public OutBuyItemDTO toDTO(){
        return new OutBuyItemDTO(id, buy.getId(), item.toDTO(), quantity);
    }
}
