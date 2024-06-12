package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import br.edu.ifsp.galaxyfood.server.model.domain.ComboItem;
import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ComboItemDAO extends JpaRepository<ComboItem, UUID> {

    @Query(value = "SELECT c FROM ComboItem c WHERE c.id = ?1")
    ComboItem getComboItemById(UUID id);

    @Query(value = "SELECT case when count(c)> 0 then true else false end FROM ComboItem c WHERE c.combo = ?1 and c.item = ?2")
    boolean existsByParams(Combo combo, Food food);

    @Query(value = "SELECT c FROM ComboItem c WHERE c.combo = ?1 and c.item = ?2")
    ComboItem getComboItemByParams(Combo combo, Food food);
}
