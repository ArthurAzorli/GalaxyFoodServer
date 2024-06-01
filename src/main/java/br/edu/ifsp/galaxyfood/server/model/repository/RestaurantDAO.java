package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.Restaurant;
import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RestaurantDAO extends JpaRepository<Restaurant, UUID> {


    Restaurant getRestaurantById(UUID id);

    Restaurant getRestaurantByEmail(String email);

    @Query(value = "SELECT r FROM Restaurant r")
    List<Restaurant> getAllRestaurant();

    @Query(value = "SELECT r FROM Restaurant r WHERE r.address.state = ?1 AND r.address.city = ?2")
    List<Restaurant> getAllOfLocal(String city, String state);

    @Query(value = "SELECT r FROM Restaurant r WHERE r.name LIKE %?1% OR r.specialty LIKE %?1% OR r.cnpj LIKE %?1%")
    List<Restaurant> search(String text);

    @Query(value = "SELECT r FROM Restaurant r WHERE r.address.state = ?2 AND r.address.city = ?3 AND (r.name LIKE %?1% OR r.specialty LIKE %?1% OR r.cnpj LIKE %?1%)")
    List<Restaurant> searchOfLocal(String text, String city, String state);

    int countByOwner(RestaurantOwner owner);

    boolean existsRestaurantByEmail(String email);

    boolean existsRestaurantByCnpj(String cnpj);

}
