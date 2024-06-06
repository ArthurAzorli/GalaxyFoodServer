package br.edu.ifsp.galaxyfood.server.model.dto;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.Phone;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OutClientDTO (UUID id, String cpf, String name, String email, LocalDate birthDate, byte[] image, List<Address> addresses, List<Phone> phones){
}
