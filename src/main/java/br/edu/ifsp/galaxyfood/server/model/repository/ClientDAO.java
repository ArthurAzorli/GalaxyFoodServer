package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ClientDAO extends JpaRepository<Client, UUID> {

    boolean existsClientByCpf(String cpf);

    boolean existsClientByEmail(String email);

    @Query(value = "SELECT c FROM Client c WHERE c.email = ?1")
    Client getByEmail(String email);

    @Query(value = "SELECT c FROM Client c WHERE c.id = ?1")
    Client getClientById(UUID id);

}
