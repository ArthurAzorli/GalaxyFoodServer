package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.UUID;

public record InComboItemDTO(UUID combo, UUID item, Integer quantity) {
}
