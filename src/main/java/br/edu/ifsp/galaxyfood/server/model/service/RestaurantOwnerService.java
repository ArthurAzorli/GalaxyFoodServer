package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.RestaurantOwner;
import br.edu.ifsp.galaxyfood.server.model.dto.InRestaurantOwnerDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.LoginDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.RestaurantDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.RestaurantOwnerDAO;
import br.edu.ifsp.galaxyfood.server.utils.Cripto;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantOwnerService {

    private final RestaurantOwnerDAO repository;

    private final RestaurantDAO restaurantDAO;

    public RestaurantOwner create(InRestaurantOwnerDTO dto) throws ExceptionController {

        if (dto.rg() == null) throw new ExceptionController(400, "RG not send!");
        if (dto.cpf() == null) throw new ExceptionController(400, "CPF not send!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not send!");
        if (dto.birthDate() == null) throw new ExceptionController(400, "Birth date not send!");


        if (repository.existsByCpf(dto.cpf())) throw new ExceptionController(409, "CPF: " + dto.cpf() + " já está cadastrado!");
        if (repository.existsByRg(dto.rg())) throw new ExceptionController(409, "RG: " + dto.rg() + " já está cadastrado!");

        var owner = new RestaurantOwner(dto.rg(), dto.cpf(), dto.name(), dto.birthDate());

        return repository.save(owner);
    }

    public RestaurantOwner get(UUID id) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (!repository.existsById(id)) throw new ExceptionController(404, "Dono não cadastrado!");

        return repository.getOwnerById(id);
    }

    public RestaurantOwner update(UUID idRestaurant, InRestaurantOwnerDTO dto) throws ExceptionController{
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not send!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not send!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        var owner = restaurant.getOwner();

        owner.setName(dto.name());

        return repository.save(owner);
    }

    public void delete(LoginDTO dto) throws ExceptionController{
        if (dto.login() == null || dto.login().isEmpty()) throw new ExceptionController(400, "Login not sent!");
        if (dto.password() == null || dto.password().isEmpty()) throw new ExceptionController(400, "Password not sent!");

        if (!restaurantDAO.existsRestaurantByEmail(dto.login())) throw new ExceptionController(404, "Restaurante não encontrado!");

        var restaurant = restaurantDAO.getRestaurantByEmail(dto.login());

        if (!restaurant.getPassword().equals(Cripto.md5(dto.password()))) throw new ExceptionController(400, "Login e/ou Senha incorreta!");

        if (restaurantDAO.countByOwner(restaurant.getOwner())>1) throw new ExceptionController(401, "Este dono ainda possui outros restaurantes cadastrados!");

        restaurantDAO.delete(restaurant);
        repository.delete(restaurant.getOwner());

        if (repository.existsById(restaurant.getOwner().getId())) throw new ExceptionController(500, "Erro ao deletar Cliente!");
    }
}
