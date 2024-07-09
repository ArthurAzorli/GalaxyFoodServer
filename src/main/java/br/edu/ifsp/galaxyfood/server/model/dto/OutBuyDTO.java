package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OutBuyDTO(UUID id, int paymentForm, int orderStatus, LocalDateTime date, BigDecimal deliveryFee, BigDecimal discount, Address sentAddress, OutClientDTO client, OutRestaurantDTO restaurant, List<OutBuyItemDTO> items) {
}
