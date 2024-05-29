package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.ClientPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientPhoneDAO extends JpaRepository<ClientPhone, String> {

    ClientPhone getClientPhoneByPhone(String phone);
}
