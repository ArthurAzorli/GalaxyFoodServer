package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutRestaurantOwnerDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "restaurant_owner")
public class RestaurantOwner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR")
    private UUID id;

    @Column(length = 14)
    private String cpf;

    @Column(nullable = false, unique = true, length = 12)
    private String rg;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate birthDate;

    public RestaurantOwner(String cpf, String rg, String name, LocalDate birthDate) {
        this.cpf = cpf;
        this.rg = rg;
        this.name = name;
        this.birthDate = birthDate;
    }

    public RestaurantOwner() {
    }

    public OutRestaurantOwnerDTO toDTO(){
        return new OutRestaurantOwnerDTO(id, rg, cpf, name, birthDate);
    }
}
