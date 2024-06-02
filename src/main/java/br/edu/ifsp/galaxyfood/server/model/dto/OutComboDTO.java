package br.edu.ifsp.galaxyfood.server.model.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OutComboDTO(UUID id, String name, BigDecimal price, byte[] image, UUID parent, List<OutComboItemDTO> items) {
}
