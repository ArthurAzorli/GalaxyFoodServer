package br.edu.ifsp.galaxyfood.server.model.dto;

import java.time.LocalDate;

public record InClientDTO(String cpf, String name, String email, LocalDate birthDate, byte[] image, String password) {
}
