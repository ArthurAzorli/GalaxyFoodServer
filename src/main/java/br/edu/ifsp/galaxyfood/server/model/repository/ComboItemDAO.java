package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.ComboItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ComboItemDAO extends JpaRepository<ComboItem, UUID> {

    @Query(value = "SELECT c FROM ComboItem c WHERE c.id = ?1")
    ComboItem getComboItemById(UUID id);
}
