package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FoodDAO extends JpaRepository<Food, UUID> {
    Food getFoodById(UUID id);

    @Query(value = "SELECT f FROM Food f, Package p WHERE f.parent.id = p.id AND p.restaurant.id = ?1")
    List<Food> getAllByRestaurant(UUID idRestaurant);


}
