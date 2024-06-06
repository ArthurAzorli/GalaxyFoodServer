package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.Phone;

import java.util.List;
import java.util.UUID;


public record OutRestaurantDTO(UUID id, String cnpj, String email, String name, String specialty, byte[] image, double score, List<OutScoreDTO> scores, Address address, OutRestaurantOwnerDTO owner, List<Phone> phones) {
}
