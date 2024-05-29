package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyDAO extends JpaRepository<Buy, UUID> {

    Buy getBuyById(UUID id);
}
