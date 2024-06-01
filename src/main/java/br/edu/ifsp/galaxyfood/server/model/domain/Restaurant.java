package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.utils.Cripto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR")
    private UUID id;

    @Column(length = 18, nullable = false, unique = true)
    private String cnpj;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialty;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(columnDefinition = "DECIMAL")
    private BigDecimal score;

    @Column(nullable = false)
    private int countScore;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "phone", cascade = CascadeType.ALL)
    private List<ClientPhone> phones = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="owner_id", nullable=false, columnDefinition = "VARCHAR", referencedColumnName = "id")
    private RestaurantOwner owner;

    public Restaurant(UUID id, String cnpj, String email, String name, String specialty, byte[] image, String password, BigDecimal score, Integer countScore, Address address, RestaurantOwner owner, List<ClientPhone> phones) {
        this.id = id;
        this.cnpj = cnpj;
        this.email = email;
        this.name = name;
        this.specialty = specialty;
        this.image = image;
        this.password = Cripto.md5(password);
        this.score = score;
        this.countScore = countScore;
        this.address = address;
        this.owner = owner;
        this.phones = phones;
    }

    public Restaurant(String cnpj, String email, String name, String specialty, byte[] image, String password, BigDecimal score, Integer countScore, Address address, RestaurantOwner owner) {
        this.id = UUID.randomUUID();
        this.cnpj = cnpj;
        this.email = email;
        this.name = name;
        this.specialty = specialty;
        this.image = image;
        this.password = Cripto.md5(password);
        this.score = score;
        this.countScore = countScore;
        this.address = address;
        this.owner = owner;
    }

    public Restaurant() {
    }

    public void setPassword(String password) {
        this.password = Cripto.md5(password);
    }

    public boolean addPhone(ClientPhone phone) {
        if (phones.contains(phone)) return false;
        return phones.add(phone);
    }

}
