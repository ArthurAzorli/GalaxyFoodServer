package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.UUID;

public record InAddressDTO(UUID id, String street, String number, String neighborhood, String city, String state, String cep) {
}
