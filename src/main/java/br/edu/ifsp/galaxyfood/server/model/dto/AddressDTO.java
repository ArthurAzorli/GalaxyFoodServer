package br.edu.ifsp.galaxyfood.server.model.dto;

public record AddressDTO(String street, String number, String neighborhood, String city, String state, String cep) {
}
