package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.ClientPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientPhoneDAO extends JpaRepository<ClientPhone, String> {

    @Query(value = "SELECT c FROM ClientPhone c WHERE c.phone = ?1")
    ClientPhone getClientPhoneByPhone(String phone);
}
