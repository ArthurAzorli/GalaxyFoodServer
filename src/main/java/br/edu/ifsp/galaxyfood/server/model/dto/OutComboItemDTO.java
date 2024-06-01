package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.UUID;

public record OutComboItemDTO(UUID id, Integer quantity, UUID combo, OutFoodDTO item) {
}
