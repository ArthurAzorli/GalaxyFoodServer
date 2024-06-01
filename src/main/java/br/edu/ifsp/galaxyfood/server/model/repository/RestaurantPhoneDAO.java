package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantPhoneDAO extends JpaRepository<RestaurantPhone, String> {

    RestaurantPhone getRestaurantPhoneByPhone(String phone);
}
