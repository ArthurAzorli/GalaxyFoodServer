package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.PaymentForm;

import java.util.List;
import java.util.UUID;

public record InBuyDTO(PaymentForm paymentForm, UUID sentAddress, UUID restaurant, List<InBuyItemDTO> items) {
}
