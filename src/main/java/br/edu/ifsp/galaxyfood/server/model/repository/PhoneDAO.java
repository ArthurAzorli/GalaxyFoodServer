package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PhoneDAO extends JpaRepository<Phone, UUID> {

    @Query("SELECT P FROM Phone p WHERE p.id = ?1")
    Phone getPhoneById(UUID id);
}
