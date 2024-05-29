package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantOwnerDAO extends JpaRepository<RestaurantOwner, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByRg(String rg);

    RestaurantOwner getOwnerById(UUID id);
}
