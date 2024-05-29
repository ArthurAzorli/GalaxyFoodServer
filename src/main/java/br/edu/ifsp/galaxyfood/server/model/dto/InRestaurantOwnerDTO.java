package br.edu.ifsp.galaxyfood.server.model.dto;

import java.time.LocalDate;

public record InRestaurantOwnerDTO (String rg, String cpf, String name, LocalDate birthDate){
}
