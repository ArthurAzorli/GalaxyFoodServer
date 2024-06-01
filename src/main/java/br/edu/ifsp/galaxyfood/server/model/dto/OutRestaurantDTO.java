package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.ClientPhone;
import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record OutRestaurantDTO(UUID id, String cnpj, String email, String name, String specialty, byte[] image, BigDecimal score, Integer countScore, Address address, OutRestaurantOwnerDTO owner, List<PhoneDTO> phones) {
}
