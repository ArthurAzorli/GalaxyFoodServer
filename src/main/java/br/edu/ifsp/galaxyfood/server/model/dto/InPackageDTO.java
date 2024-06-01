package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Package;

import java.util.UUID;

public record InPackageDTO(String name, byte[] image, UUID parent) {}