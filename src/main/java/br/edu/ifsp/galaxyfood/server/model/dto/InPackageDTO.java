package br.edu.ifsp.galaxyfood.server.model.dto;

import java.util.UUID;

public record InPackageDTO(String name, byte[] image, UUID parent) {}