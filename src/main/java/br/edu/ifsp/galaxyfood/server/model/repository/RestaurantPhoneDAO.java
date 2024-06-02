package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantPhoneDAO extends JpaRepository<RestaurantPhone, String> {

    @Query(value = "SELECT r FROM RestaurantPhone r WHERE r.phone = ?1")
    RestaurantPhone getRestaurantPhoneByPhone(String phone);
}
