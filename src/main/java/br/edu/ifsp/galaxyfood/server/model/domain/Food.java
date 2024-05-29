package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutFoodDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Food extends PackageItem {

    @Column(columnDefinition = "TEXT")
    private String description;

    public Food(UUID id, String name, BigDecimal price, String description, byte[] image, Package parent) {
        super.id = id;
        super.name = name;
        super.price = price;
        super.parent = parent;
        super.image = image;
        this.description = description;
    }

    public Food(String name, BigDecimal price, String description, byte[] image, Package parent) {
        super.id = UUID.randomUUID();
        super.name = name;
        super.price = price;
        super.parent = parent;
        super.image = image;
        this.description = description;
    }

    public Food() {
    }

    public OutFoodDTO toDTO(){
        return  new OutFoodDTO(id, name, price, description ,image, parent.getId());
    }
}
