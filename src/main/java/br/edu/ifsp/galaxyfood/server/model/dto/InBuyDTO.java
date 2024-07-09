package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.OrderStatus;
import br.edu.ifsp.galaxyfood.server.model.domain.PaymentForm;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record InBuyDTO(PaymentForm paymentForm, OrderStatus orderStatus, BigDecimal deliveryFee, BigDecimal discount, UUID sentAddress, UUID restaurant, List<InBuyItemDTO> items) {
}
