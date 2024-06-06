package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PhoneDAO extends JpaRepository<Phone, UUID> {

    @Query("SELECT p FROM Phone p WHERE p.id = ?1")
    Phone getPhoneById(UUID id);

    @Query(value = "SELECT case when count(p)> 0 then true else false end FROM Phone p WHERE p.phone = ?1")
    boolean existsByPhone(String phone);
}
