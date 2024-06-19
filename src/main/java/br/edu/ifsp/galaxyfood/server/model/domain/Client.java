package br.edu.ifsp.galaxyfood.server.model.domain;

import br.edu.ifsp.galaxyfood.server.model.dto.OutClientDTO;
import br.edu.ifsp.galaxyfood.server.utils.Cripto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 14, unique = true, updatable = false)
    private String cpf;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(nullable = false, length = 32)
    private String password;

    @OneToMany(cascade = CascadeType.REMOVE , orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    public Client(UUID id, String cpf, String name, String email, LocalDate birthDate, byte[] image, String password, List<Address> addresses, List<Phone> phones) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.image = image;
        this.password = Cripto.md5(password);
        this.addresses = addresses;
        this.phones = phones;
    }

    public Client(String cpf, String name, String email, LocalDate birthDate, byte[] image, String password, List<Address> addresses, List<Phone> phones) {
        this.id = UUID.randomUUID();
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.image = image;
        this.password = Cripto.md5(password);
        this.addresses = addresses;
        this.phones = phones;
    }

    public Client(String cpf, String name, String email, LocalDate birthDate, byte[] image, String password) {
        this.id = UUID.randomUUID();
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.image = image;
        this.password = Cripto.md5(password);
    }

    public Client() {}

    public void setPassword(String password) {
        this.password = Cripto.md5(password);
    }

    public boolean addPhone(Phone phone){
        if (phones.contains(phone)) return false;
        return phones.add(phone);
    }

    public boolean addAddress(Address address){
        if (addresses.contains(address)) return false;
        return addresses.add(address);
    }


    public OutClientDTO toDTO(){
        return new OutClientDTO(id, cpf, name, email, birthDate, image, addresses, phones);
    }
}
