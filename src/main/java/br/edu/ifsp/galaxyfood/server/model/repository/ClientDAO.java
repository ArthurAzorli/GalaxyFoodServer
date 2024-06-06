package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ClientDAO extends JpaRepository<Client, UUID> {

    @Query(value = "SELECT case when count(c)> 0 then true else false end FROM Client c WHERE c.cpf = ?1")
    boolean existsClientByCpf(String cpf);

    @Query(value = "SELECT case when count(c)> 0 then true else false end FROM Client c WHERE c.email = ?1")
    boolean existsClientByEmail(String email);

    @Query(value = "SELECT c FROM Client c WHERE c.email = ?1")
    Client getByEmail(String email);

    @Query(value = "SELECT c FROM Client c WHERE c.id = ?1")
    Client getClientById(UUID id);

}
