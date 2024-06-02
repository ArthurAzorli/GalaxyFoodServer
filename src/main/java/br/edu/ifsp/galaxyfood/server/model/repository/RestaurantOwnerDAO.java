package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RestaurantOwnerDAO extends JpaRepository<RestaurantOwner, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByRg(String rg);

    @Query(value = "SELECT r FROM RestaurantOwner r WHERE r.id = ?1")
    RestaurantOwner getOwnerById(UUID id);
}
