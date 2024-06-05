package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.UUID;

public record InRestaurantDTO(String cnpj, String email, String name, String specialty, byte[] image, String password, InAddressDTO address, UUID owner) {
}
