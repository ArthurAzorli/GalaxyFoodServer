package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Restaurant;
import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RestaurantDAO extends JpaRepository<Restaurant, UUID> {


    Restaurant getRestaurantById(UUID id);

    Restaurant getRestaurantByEmail(String email);

    int countByOwner(RestaurantOwner owner);

    boolean existsRestaurantByEmail(String email);

    boolean existsRestaurantByCnpj(String cnpj);

}
