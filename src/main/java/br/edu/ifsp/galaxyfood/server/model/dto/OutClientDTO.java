package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.ClientAddress;
import br.edu.ifsp.galaxyfood.server.model.domain.ClientPhone;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OutClientDTO (UUID id, String cpf, String name, String email, LocalDate birthDate, byte[] image, List<ClientAddress> addresses, List<ClientPhone> phones){
}
