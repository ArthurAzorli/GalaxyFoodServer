package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.List;
import java.util.UUID;

public record OutPackageDTO(UUID id, String name, byte[] image, UUID parent, UUID restaurant, List<OutPackageDTO> children, List<Object> items) {
}
