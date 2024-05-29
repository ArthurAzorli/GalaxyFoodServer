package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.BuyItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyItemDAO extends JpaRepository<BuyItem, UUID> {
}
