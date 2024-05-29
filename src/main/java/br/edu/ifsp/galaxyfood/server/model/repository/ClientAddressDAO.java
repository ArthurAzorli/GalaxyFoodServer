package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientAddressDAO  extends JpaRepository<ClientAddress, UUID> {

    ClientAddress getAddressById(UUID id);
}
