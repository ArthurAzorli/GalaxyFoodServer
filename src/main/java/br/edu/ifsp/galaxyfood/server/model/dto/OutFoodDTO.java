package br.edu.ifsp.galaxyfood.server.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OutFoodDTO(UUID id,String name, BigDecimal price, String description, byte[] image, UUID parent) {
}
