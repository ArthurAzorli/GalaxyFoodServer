package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.UUID;

public record OutBuyItemDTO(UUID id, UUID buy, OutPackageItemDTO item, int quantity) {
}
