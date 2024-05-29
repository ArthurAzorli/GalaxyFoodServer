package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Package;
import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OutComboDTO(UUID id, String name, BigDecimal price, byte[] image, Package parent, List<PackageItem> items) {
}
