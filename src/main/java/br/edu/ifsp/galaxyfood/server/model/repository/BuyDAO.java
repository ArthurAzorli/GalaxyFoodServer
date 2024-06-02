package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BuyDAO extends JpaRepository<Buy, UUID> {

    @Query(value = "SELECT b FROM Buy WHERE b.id = ?1")
    Buy getBuyById(UUID id);

    @Query(value = "SELECT b FROM Buy b WHERE b.client.id = ?1")
    List<Buy> getAllByClient(UUID idClient);

    @Query(value = "SELECT b FROM Buy b WHERE b.restaurant.id = ?1")
    List<Buy> getAllByRestaurant(UUID idRestaurant);
}
