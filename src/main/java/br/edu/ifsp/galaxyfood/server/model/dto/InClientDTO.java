package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.ClientPhone;

import java.time.LocalDate;
import java.util.List;

public record InClientDTO(String cpf, String name, String email, LocalDate birthDate, byte[] image, String password, List<Address> addresses, List<ClientPhone> phones) {
}
