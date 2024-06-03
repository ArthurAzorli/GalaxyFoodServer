package br.edu.ifsp.galaxyfood.server.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OutScoreDTO(UUID id, BigDecimal score, UUID client, UUID restaurant) {
}
