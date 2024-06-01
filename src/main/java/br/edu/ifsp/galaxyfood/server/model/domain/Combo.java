package br.edu.ifsp.galaxyfood.server.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Combo extends PackageItem {

    @OneToMany
    private List<ComboItem> items = new ArrayList<>();

    public Combo(UUID id, String name, BigDecimal price, byte[] image, Package parent, List<ComboItem> items) {
        super.id = id;
        super.name = name;
        super.price = price;
        super.image = image;
        super.parent = parent;
        this.items = items;
    }

    public Combo(String name, BigDecimal price, byte[] image, Package parent, List<ComboItem> items) {
        super.id = UUID.randomUUID();
        super.name = name;
        super.price = price;
        super.image = image;
        super.parent = parent;
        this.items = items;
    }

    public Combo(UUID id, String name, BigDecimal price, byte[] image, Package parent) {
        super.id = id;
        super.name = name;
        super.price = price;
        super.image = image;
        super.parent = parent;
    }

    public Combo(String name, BigDecimal price, byte[] image, Package parent) {
        super.id = UUID.randomUUID();
        super.name = name;
        super.price = price;
        super.image = image;
        super.parent = parent;;
    }

    public Combo() {
    }

    public boolean addItem(ComboItem item){
        if (items.contains(item)) return false;
        return items.add(item);
    }

}
