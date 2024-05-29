package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.Client;
import br.edu.ifsp.galaxyfood.server.model.domain.PaymentForm;
import br.edu.ifsp.galaxyfood.server.model.domain.Restaurant;

import java.time.LocalDateTime;

public record InBuyDTO(PaymentForm paymentForm, LocalDateTime date, Address sentAddress, Client client, Restaurant restaurant) {
}
