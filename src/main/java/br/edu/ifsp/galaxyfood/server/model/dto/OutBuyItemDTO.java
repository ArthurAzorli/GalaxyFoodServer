package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;

import java.util.UUID;

public record OutBuyItemDTO(UUID id, UUID buy, OutPackageItemDTO item, int quantity) {
}
