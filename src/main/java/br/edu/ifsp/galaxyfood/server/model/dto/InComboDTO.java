package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Package;

import java.math.BigDecimal;
import java.util.UUID;

public record InComboDTO(String name, BigDecimal price, byte[] image, UUID parent) {
}
