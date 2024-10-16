package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Address;
import br.edu.ifsp.galaxyfood.server.model.domain.Client;
import br.edu.ifsp.galaxyfood.server.model.domain.Phone;
import br.edu.ifsp.galaxyfood.server.model.dto.InAddressDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InClientDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.LoginDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.AddressDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.BuyDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.ClientDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.PhoneDAO;
import br.edu.ifsp.galaxyfood.server.utils.Cripto;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientDAO clientDAO;

    private final AddressDAO addressDAO;

    private final PhoneDAO phoneDAO;

    private final BuyDAO buyDAO;

    public Client login(String login, String password) throws ExceptionController {
        if (login == null || login.isEmpty()) throw new ExceptionController(400, "Login not send!");
        if (password == null || password.isEmpty()) throw new ExceptionController(400, "Password not send!");

        if (!clientDAO.existsClientByEmail(login)) throw new ExceptionController(404, "Cliente não encontrado!");

        var client = clientDAO.getByEmail(login);

        if (!client.getPassword().equals(Cripto.md5(password))) throw new ExceptionController(400, "Login e/ou Senha incorreta!");

        return client;
    }

    public Client create(InClientDTO dto) throws ExceptionController {

        if (dto.cpf() == null) throw new ExceptionController(400, "CPF not send!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not send!");
        if (dto.email() == null) throw new ExceptionController(400, "Email not send!");
        if (dto.birthDate() == null) throw new ExceptionController(400, "Birth date not send!");
        if (dto.password() == null) throw new ExceptionController(400, "Password not send!");

        if (clientDAO.existsClientByCpf(dto.cpf())) throw new ExceptionController(409, "CPF: "+dto.cpf()+" já está cadastrado!");
        if (clientDAO.existsClientByEmail(dto.email())) throw new ExceptionController(409, "E-mail: "+dto.email()+" já está cadastrado!");

        var client = new Client(dto.cpf(), dto.name(), dto.email(), dto.birthDate(), dto.image(), dto.password());

        return clientDAO.save(client);
    }

    public Client get(UUID id){
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (!clientDAO.existsById(id)) throw new ExceptionController(404, "Cliente não cadastrado!");

        return clientDAO.getClientById(id);
    }

    public Client update(UUID id, InClientDTO dto) throws ExceptionController{

        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not send!");
        if (dto.email() == null) throw new ExceptionController(400, "Email not send!");

        if (!clientDAO.existsById(id)) throw new ExceptionController(412, "Cliente não cadastrado!");

        var client = clientDAO.getClientById(id);
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setImage(dto.image());

        return clientDAO.save(client);
    }

    public void changePassword(UUID id, String oldPassword, String newPassword){
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (oldPassword == null || oldPassword.isEmpty()) throw new ExceptionController(400, "Old password not send!");
        if (newPassword == null || newPassword.isEmpty()) throw new ExceptionController(400, "New password not send!");

        if (!clientDAO.existsById(id)) throw new ExceptionController(412, "Cliente não cadastrado!");

        var client = clientDAO.getClientById(id);

        if (!client.getPassword().equals(Cripto.md5(oldPassword))) throw new ExceptionController(412, "Senha incorreta!");
        if (oldPassword.equals(newPassword)) throw new ExceptionController(400, "Nova senha é a mesma que a antiga!");

        client.setPassword(newPassword);

        clientDAO.save(client);
    }

    public Client addPhone(UUID id, String phone){
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (phone == null) throw new ExceptionController(400, "Phone not send!");

        if (!clientDAO.existsById(id)) throw new ExceptionController(412, "Cliente não cadastrado!");

        var client = clientDAO.getClientById(id);

        var newPhone = new Phone(phone);

        if (!client.addPhone(newPhone)) throw new ExceptionController(409, "Telefone já está cadastrado!");
        if (phoneDAO.existsByPhone(phone)) throw new ExceptionController(409, "Telefone cadastrdo em outro Usuário!");

        phoneDAO.save(newPhone);
        return clientDAO.save(client);
    }

    public Client remPhone(UUID id, UUID idPhone){
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (idPhone == null) throw new ExceptionController(400, "ID Phone not send!");

        if (!clientDAO.existsById(id)) throw new ExceptionController(412, "Cliente não cadastrado!");

        if (!phoneDAO.existsById(idPhone)) throw new ExceptionController(404, "Telefone não encontrado!");

        var phone = phoneDAO.getPhoneById(idPhone);
        var client = clientDAO.getClientById(id);

        if (!client.getPhones().contains(phone)) throw new ExceptionController(404, "Telefone não está cadastrado!");

        client.getPhones().remove(phone);
        client =  clientDAO.save(client);
        phoneDAO.delete(phone);

        return client;
    }

    public Client addAddress(UUID id, InAddressDTO dto){
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (dto.street() == null) throw new ExceptionController(400, "Street not send!");
        if (dto.number() == null) throw new ExceptionController(400, "Number not send!");
        if (dto.neighborhood() == null) throw new ExceptionController(400, "Neighborhood not send!");
        if (dto.city() == null) throw new ExceptionController(400, "City not send!");
        if (dto.state() == null) throw new ExceptionController(400, "State not send!");
        if (dto.cep() == null ) throw new ExceptionController(400, "CEP not send!");

        var address = new Address(dto.street(), dto.number(), dto.neighborhood(), dto.city(), dto.state(), dto.cep());

        if (!clientDAO.existsById(id)) throw new ExceptionController(412, "Cliente não cadastrado!");

        var client = clientDAO.getClientById(id);

        address = addressDAO.save(address);

        if (!client.addAddress(address)) {
            addressDAO.delete(address);
            throw new ExceptionController(409, "Endereço já está cadastrado!");
        }


        return clientDAO.save(client);
    }

    public Client remAddress(UUID id, UUID idAddress){
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (idAddress == null) throw new ExceptionController(400, "Address id not send!");

        if (!clientDAO.existsById(id)) throw new ExceptionController(412, "Cliente não cadastrado!");

        if (!addressDAO.existsById(idAddress)) throw new ExceptionController(409, "Endereço não encontrado!");

        var address = addressDAO.getAddressById(idAddress);

        var client = clientDAO.getClientById(id);

        if (!client.getAddresses().contains(address)) throw new ExceptionController(409, "Enderço não está cadstrado!");

        client.getAddresses().remove(address);

        clientDAO.save(client);
        addressDAO.delete(address);
        return client;
    }

    public void delete(LoginDTO dto) throws ExceptionController {
        if (dto.login() == null || dto.login().isEmpty()) throw new ExceptionController(400, "Login not send!");
        if (dto.password() == null || dto.password().isEmpty()) throw new ExceptionController(400, "Password not send!");

        if (!clientDAO.existsClientByEmail(dto.login())) throw new ExceptionController(404, "Cliente não encontrado!");

        var client = clientDAO.getByEmail(dto.login());

        if (!client.getPassword().equals(Cripto.md5(dto.password()))) throw new ExceptionController(400, "Login e/ou Senha incorreta!");

        for (var buy : buyDAO.getAllByClient(client.getId())){
            buy.setSentAddress(null);
            buy.setClient(null);
            buyDAO.save(buy);
        }

        final var adresses = client.getAddresses();

        client.getAddresses().clear();
        clientDAO.save(client);

        for (var address : adresses){
            addressDAO.delete(address);
        }

        clientDAO.deleteById(client.getId());

        if (clientDAO.existsById(client.getId())) throw new ExceptionController(500, "Erro ao deletar Cliente!");
    }
}
