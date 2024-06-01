package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ComboDAO extends JpaRepository<Combo, UUID> {

    Combo getComboById(UUID id);

    @Query(value = "SELECT c FROM Combo c, Package p WHERE c.parent.id = p.id AND p.restaurant.id = ?1")
    List<Combo> getAllByRestaurant(UUID idRestaurant);
}
