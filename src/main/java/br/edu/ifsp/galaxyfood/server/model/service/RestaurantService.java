package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.*;
import br.edu.ifsp.galaxyfood.server.model.dto.InRestaurantDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.LoginDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.Cripto;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantDAO restaurantDAO;

    private final RestaurantOwnerDAO ownerDAO;

    private final PackageDAO packageDAO;

    private final AddressDAO addressDAO;

    private final ClientDAO clientDAO;

    private final ScoreDAO scoreDAO;

    private final PhoneDAO phoneDAO;

    private final BuyDAO buyDAO;

    public Restaurant login(String login, String password) throws ExceptionController {
        if (login == null || login.isEmpty()) throw new ExceptionController(400, "Login not sent!");
        if (password == null || password.isEmpty()) throw new ExceptionController(400, "Password not sent!");

        if (!restaurantDAO.existsRestaurantByEmail(login)) throw new ExceptionController(404, "Restaurante não encontrado!");

        var restaurant = restaurantDAO.getRestaurantByEmail(login);

        if (!restaurant.getPassword().equals(Cripto.md5(password))) throw new ExceptionController(400, "Login e/ou Senha incorreta!");

        return restaurant;
    }

    public Restaurant create(InRestaurantDTO dto) throws ExceptionController {

        if (dto.cnpj() == null) throw new ExceptionController(400, "CNPJ not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.email() == null) throw new ExceptionController(400, "Email not sent!");
        if (dto.specialty() == null) throw new ExceptionController(400, "Specialty not sent!");
        if (dto.password() == null) throw new ExceptionController(400, "Password not sent!");
        if (dto.address() == null) throw new ExceptionController(400, "Address not sent!");
        if (dto.address().street() == null) throw new ExceptionController(400, "Street not sent!");
        if (dto.address().number() == null) throw new ExceptionController(400, "Number not sent!");
        if (dto.address().neighborhood() == null) throw new ExceptionController(400, "Neighborhood not sent!");
        if (dto.address().city() == null) throw new ExceptionController(400, "City not sent!");
        if (dto.address().state() == null) throw new ExceptionController(400, "State not sent!");
        if (dto.address().cep() == null) throw new ExceptionController(400, "CEP not sent!");
        if (dto.owner() == null) throw new ExceptionController(400, "Owner id not sent");

        if (restaurantDAO.existsRestaurantByCnpj(dto.cnpj())) throw new ExceptionController(409, "CNPJ: "+dto.cnpj()+" já está cadastrado!");
        if (restaurantDAO.existsRestaurantByEmail(dto.email())) throw new ExceptionController(409, "E-mail: "+dto.email()+" já está cadastrado!");

        if (!ownerDAO.existsById(dto.owner())) throw new ExceptionController(404, "Dono não encontrado!");

        var owner = ownerDAO.getOwnerById(dto.owner());
        var address = addressDAO.save(new Address(dto.address().street(), dto.address().number(), dto.address().neighborhood(), dto.address().city(), dto.address().state(), dto.address().cep()));;
        return restaurantDAO.save(new Restaurant(dto.cnpj(), dto.email(), dto.name(), dto.specialty(), dto.image(), dto.password(), address, owner));
    }

    public Restaurant get(UUID id) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não cadastrado!");

        return restaurantDAO.getRestaurantById(id);
    }

    public List<Restaurant> getAll() throws ExceptionController {
        return restaurantDAO.getAllRestaurant();
    }

    public List<Restaurant> getAllOfLocal(UUID idAddress) throws ExceptionController {
        if (idAddress == null) throw new ExceptionController(400, "Address id not sent!");

        if (!addressDAO.existsById(idAddress)) throw new ExceptionController(404, "Endereço não encontrado!");

        var address = addressDAO.getAddressById(idAddress);

        return restaurantDAO.getAllOfLocal(address.getCity(), address.getState());
    }

    public List<Restaurant> search(String text) throws ExceptionController {
        if (text == null) throw new ExceptionController(400, "Search text not sent!");

        return restaurantDAO.search(text.toLowerCase());
    }

    public List<Restaurant> searchOfLocal(String text, UUID idAddress){
        if (text == null) throw new ExceptionController(400, "Search text not sent!");
        if (idAddress == null) throw new ExceptionController(400, "Address id not sent!");

        if (!addressDAO.existsById(idAddress)) throw new ExceptionController(404, "Endereço não encontrado!");

        var address = addressDAO.getAddressById(idAddress);

        return restaurantDAO.searchOfLocal(text.toLowerCase(), address.getCity(), address.getState());
    }

    public Restaurant update(UUID id, InRestaurantDTO dto) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.email() == null) throw new ExceptionController(400, "Email not sent!");
        if (dto.specialty() == null) throw new ExceptionController(400, "Specialty not sent!");
        if (dto.address() == null) throw new ExceptionController(400, "Address not sent!");
        if (dto.address().street() == null) throw new ExceptionController(400, "Street not sent!");
        if (dto.address().number() == null) throw new ExceptionController(400, "Number not sent!");
        if (dto.address().neighborhood() == null) throw new ExceptionController(400, "Neighborhood not sent!");
        if (dto.address().city() == null) throw new ExceptionController(400, "City not sent!");
        if (dto.address().state() == null) throw new ExceptionController(400, "State not sent!");
        if (dto.address().cep() == null) throw new ExceptionController(400, "CEP not sent!");


        if (!restaurantDAO.existsById(id)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var address = restaurant.getAddress();

        restaurant.setName(dto.name());
        restaurant.setEmail(dto.email());
        restaurant.setImage(dto.image());
        restaurant.setSpecialty(dto.specialty());
        address.setStreet(dto.address().street());
        address.setNumber(dto.address().number());
        address.setNeighborhood(dto.address().neighborhood());
        address.setCity(dto.address().city());
        address.setState(dto.address().state());
        address.setCep(dto.address().cep());

        addressDAO.save(address);
        return restaurantDAO.save(restaurant);
    }

    public void changePassword(UUID id, String oldPassword, String newPassword) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not sent!");
        if (oldPassword == null || oldPassword.isEmpty()) throw new ExceptionController(400, "Old password not send!");
        if (newPassword == null || newPassword.isEmpty()) throw new ExceptionController(400, "New password not send!");

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);

        if (!restaurant.getPassword().equals(Cripto.md5(oldPassword))) throw new ExceptionController(412, "Senha incorreta!");
        if (oldPassword.equals(newPassword)) throw new ExceptionController(400, "Nova senha é a mesma que a antiga!");

        restaurant.setPassword(newPassword);

        restaurantDAO.save(restaurant);
    }

    public Restaurant addPhone(UUID id, String phone) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not sent!");
        if (phone == null) throw new ExceptionController(400, "Phone not send!");

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var newPhone = new Phone(phone);

        if (!restaurant.addPhone(newPhone)) throw new ExceptionController(409, "Telafone já cadastrado!");
        if (phoneDAO.existsByPhone(phone)) throw new ExceptionController(409, "Telefone cadastrdo em outro Usuário!");

        phoneDAO.save(newPhone);
        return  restaurantDAO.save(restaurant);
    }

    public Restaurant remPhone(UUID id, UUID idPhone) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not sent!");
        if (idPhone == null) throw new ExceptionController(400, "Phone id not send!");

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!phoneDAO.existsById(idPhone)) throw  new ExceptionController(404, "Telefone não encontrado|!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var phone = phoneDAO.getPhoneById(idPhone);

        if (!restaurant.getPhones().contains(phone)) throw new ExceptionController(404, "Telefone não está cadastrado!");

        restaurant.getPhones().remove(phone);
        restaurant = restaurantDAO.save(restaurant);
        phoneDAO.delete(phone);

        return restaurant;
    }

    public Restaurant score(UUID idRestaurant, UUID idClient, Double value) throws ExceptionController{
        if (idClient == null) throw new ExceptionController(400, "Client ID not sent!");
        if (value == null) throw new ExceptionController(400, "Score value not sent!");
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");

        if (!clientDAO.existsById(idClient)) throw new ExceptionController(412, "Cliente não cadastrado!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");

        var client = clientDAO.getClientById(idClient);
        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        for (var score : restaurant.getScore()) if (score.getClient().getId().equals(client.getId())) {
            score.setScore(BigDecimal.valueOf(value));
            scoreDAO.save(score);
            return restaurantDAO.getRestaurantById(restaurant.getId());
        }

        var score = scoreDAO.save(new Score(BigDecimal.valueOf(value), client, restaurant));

        restaurant.getScore().add(score);

        return restaurantDAO.save(restaurant);
    }

    public void delete(LoginDTO dto) throws ExceptionController{
        if (dto.login() == null || dto.login().isEmpty()) throw new ExceptionController(400, "Login not sent!");
        if (dto.password() == null || dto.password().isEmpty()) throw new ExceptionController(400, "Password not sent!");

        if (!restaurantDAO.existsRestaurantByEmail(dto.login())) throw new ExceptionController(404, "Restaurante não encontrado!");

        var restaurant = restaurantDAO.getRestaurantByEmail(dto.login());

        if (!restaurant.getPassword().equals(Cripto.md5(dto.password()))) throw new ExceptionController(400, "Login e/ou Senha incorreta!");


        for (var buy : buyDAO.getAllByRestaurant(restaurant.getId())){
            buy.setClient(null);
            buy.setSentAddress(null);
            buyDAO.save(buy);
            buyDAO.delete(buy);
        }

        final var root = packageDAO.getRoot(restaurant.getId());
        if (root != null) packageDAO.delete(root);

        restaurantDAO.delete(restaurant);

        if (restaurantDAO.existsById(restaurant.getId())) throw new ExceptionController(500, "Erro ao deletar Restaurante!");
    }
}
