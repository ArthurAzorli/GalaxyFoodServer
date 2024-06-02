package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ClientAddressDAO  extends JpaRepository<ClientAddress, UUID> {

    @Query(value = "SELECT c FROM ClientAddress c WHERE c.id = ?1")
    ClientAddress getAddressById(UUID id);
}
