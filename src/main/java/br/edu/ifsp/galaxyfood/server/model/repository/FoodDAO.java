package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodDAO extends JpaRepository<Food, UUID> {
    Food getFoodById(UUID id);
}
