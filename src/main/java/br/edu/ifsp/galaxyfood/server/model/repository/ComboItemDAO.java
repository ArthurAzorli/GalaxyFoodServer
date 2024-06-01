package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.ComboItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComboItemDAO extends JpaRepository<ComboItem, UUID> {

    ComboItem getComboItemById(UUID id);
}
