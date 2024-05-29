package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OutBuyDTO(UUID id, PaymentForm paymentForm, LocalDateTime date, Address sentAddress, Client client, Restaurant restaurant, List<BuyItem> items) {
}
