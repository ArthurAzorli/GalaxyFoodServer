package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.ClientPhone;
import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;
import br.edu.ifsp.galaxyfood.server.utils.Cripto;

import java.math.BigDecimal;
import java.util.List;

public record InRestaurantDTO(String cnpj, String email, String name, String specialty, byte[] image, String password, BigDecimal score, Address address, RestaurantOwner owner) {
}
