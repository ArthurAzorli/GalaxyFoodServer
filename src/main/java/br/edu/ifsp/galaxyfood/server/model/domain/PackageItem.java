package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutPackageItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "package_item")
public abstract class PackageItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false, columnDefinition = "DECIMAL(7,2)")
    protected BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "parent_package", referencedColumnName = "id", nullable = false)
    protected Package parent;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    protected byte[] image;

    public OutPackageItemDTO toDTO(){
        return new OutPackageItemDTO(id, name, price, image, parent.getId());
    }


}
