package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ClientDAO extends JpaRepository<Client, UUID> {

    boolean existsClientByCpf(String cpf);

    boolean existsClientByEmail(String email);

    Client getByEmail(String email);

    Client getClientById(UUID id);

}
