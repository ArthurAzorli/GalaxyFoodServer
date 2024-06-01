package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;

import java.util.UUID;

public record InBuyItemDTO(UUID idBuy, UUID idItem, Integer quantity) {
}
