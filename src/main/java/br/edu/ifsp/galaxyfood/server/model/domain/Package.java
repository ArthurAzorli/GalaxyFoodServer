package br.edu.ifsp.galaxyfood.server.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Package implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id", nullable = false, referencedColumnName = "id")
    private Restaurant restaurant;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_package", referencedColumnName = "id")
    private Package parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> children = new ArrayList<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageItem> items = new ArrayList<>();

    public Package(UUID id, String name, byte[] image, Restaurant restaurant, Package parent, List<Package> children, List<PackageItem> items) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.restaurant = restaurant;
        this.parent = parent;
        this.children = children;
        this.items = items;
    }

    public Package(UUID id, String name, byte[] image, Restaurant restaurant, Package parent) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.restaurant = restaurant;
        this.parent = parent;
    }

    public Package(String name, byte[] image, Restaurant restaurant, Package parent) {
        this.name = name;
        this.image = image;
        this.restaurant = restaurant;
        this.parent = parent;
    }

    public Package() {
    }

    public boolean addChild(Package child) {
        if (children.contains(child)) return false;
        return children.add(child);
    }

    public boolean addItem(PackageItem item) {
        if (items.contains(item)) return false;
        return items.add(item);
    }
}
