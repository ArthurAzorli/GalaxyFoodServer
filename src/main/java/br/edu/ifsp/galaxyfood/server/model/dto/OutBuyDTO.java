package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OutBuyDTO(UUID id, int paymentForm, LocalDateTime date, Address sentAddress, OutClientDTO client, OutRestaurantDTO restaurant, List<OutBuyItemDTO> items) {
}
