package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Restaurant;

public record InPackageDTO(String name, byte[] image, Restaurant restaurant) {}