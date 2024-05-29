package br.edu.ifsp.galaxyfood.server.model.dto;

import java.time.LocalDate;
import java.util.UUID;

public record OutRestaurantOwnerDTO(UUID id, String rg, String cpf, String name, LocalDate birthDate) {
}
