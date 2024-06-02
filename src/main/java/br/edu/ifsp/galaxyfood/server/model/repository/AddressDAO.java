package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AddressDAO extends JpaRepository<Address, UUID> {

    @Query(value = "SELECT a FROM Address a WHERE a.id = ?1")
    Address getAddressById(UUID id);
}
