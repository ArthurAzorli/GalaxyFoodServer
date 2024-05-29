package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComboDAO extends JpaRepository<Combo, UUID> {
}
