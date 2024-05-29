package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;

public record InBuyItemDTO(Buy buy, PackageItem item, int quantity) {
}
