package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ScoreDAO extends JpaRepository<Score, UUID> {

    @Query(value = "SELECT s FROM Score s WHERE s.id = ?1")
    Score getScoreById(UUID id);
}
