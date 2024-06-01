package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Package;
import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;
import br.edu.ifsp.galaxyfood.server.model.domain.Restaurant;

import java.util.List;
import java.util.UUID;

public record OutPackageDTO(UUID id, String name, byte[] image, UUID parent, UUID restaurant, List<OutPackageDTO> children, List<OutPackageItemDTO> items) {
}
