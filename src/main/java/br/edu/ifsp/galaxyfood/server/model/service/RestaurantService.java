package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.*;
import br.edu.ifsp.galaxyfood.server.model.dto.InRestaurantDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InScoreDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.Cripto;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantDAO restaurantDAO;

    private final RestaurantOwnerDAO ownerDAO;

    private final AddressDAO addressDAO;

    private final RestaurantPhoneDAO phoneDAO;

    private final ClientDAO clientDAO;

    private final ScoreDAO scoreDAO;

    public RestaurantService(@NonNull RestaurantDAO restaurantDAO, RestaurantOwnerDAO ownerDAO, @NonNull AddressDAO addressDAO, @NonNull RestaurantPhoneDAO phoneDAO, @NonNull ClientDAO clientDAO, @NonNull ScoreDAO scoreDAO) {
        this.restaurantDAO = restaurantDAO;
        this.ownerDAO = ownerDAO;
        this.addressDAO = addressDAO;
        this.phoneDAO = phoneDAO;
        this.clientDAO = clientDAO;
        this.scoreDAO = scoreDAO;
    }

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
        var address = new Address(dto.address().street(), dto.address().number(), dto.address().neighborhood(), dto.address().city(), dto.address().state(), dto.address().cep());
        var restaurant = restaurantDAO.save(new Restaurant(dto.cnpj(), dto.email(), dto.name(), dto.specialty(), dto.image(), dto.password(), address, owner));

        addressDAO.save(address);

        return restaurant;
    }

    public Restaurant get(UUID id) throws ExceptionController{
        if (id == null) throw new ExceptionController(400, "ID not send!");
        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não cadastrado!");

        return restaurantDAO.getRestaurantById(id);
    }

    public List<Restaurant> getAll(HttpSession session) throws ExceptionController {
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você está logado em uma conta de Restaurante!");
        return restaurantDAO.getAllRestaurant();
    }

    public List<Restaurant> getAllOfLocal(UUID idAddress, HttpSession session) throws ExceptionController {
        if (idAddress == null) throw new ExceptionController(400, "Address id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("client")) throw new ExceptionController(401, "Você não está Logado em uma conta de Cliente!");

        var id = (UUID) session.getAttribute("user");

        if (!clientDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Cliente não cadastrado!");
        }

        if (!clientDAO.existsById(id)) throw new ExceptionController(404, "Cliente não encontrado!");
        if (!addressDAO.existsById(idAddress)) throw new ExceptionController(404, "Endereço não encontrado!");

        var client = clientDAO.getClientById(id);
        var address = addressDAO.getAddressById(idAddress);

        var flag = true;
        for (var clientAddress : client.getAddresses()) if (clientAddress.getAddress().getId().equals(address.getId())) flag = false;

        if (flag) throw new ExceptionController(401, "Você não pode acessar restaurantes com base de um endereço que não seja seu!");

        return restaurantDAO.getAllOfLocal(address.getCity(), address.getState());
    }

    public List<Restaurant> search(String text, HttpSession session) throws ExceptionController {
        if (text == null) throw new ExceptionController(400, "Search text not sent!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você está logado em uma conta de Restaurante!");

        return restaurantDAO.search(text);
    }

    public List<Restaurant> searchOfLocal(String text, UUID idAddress, HttpSession session){
        if (text == null) throw new ExceptionController(400, "Search text not sent!");
        if (idAddress == null) throw new ExceptionController(400, "Address id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("client")) throw new ExceptionController(401, "Você não está Logado em uma conta de Cliente!");

        var id = (UUID) session.getAttribute("user");

        if (!clientDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Cliente não cadastrado!");
        }

        if (!addressDAO.existsById(idAddress)) throw new ExceptionController(404, "Endereço não encontrado!");

        var client = clientDAO.getClientById(id);
        var address = addressDAO.getAddressById(idAddress);

        var flag = true;
        for (var clientAddress : client.getAddresses()) if (clientAddress.getAddress().getId().equals(address.getId())) flag = false;

        if (flag) throw new ExceptionController(401, "Você não pode pesquisar restaurantes com base de um endereço que não seja seu!");

        return restaurantDAO.searchOfLocal(text, address.getCity(), address.getState());
    }

    public Restaurant update(InRestaurantDTO dto, HttpSession session) throws ExceptionController{

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


        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

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

    public void changePassword(String oldPassword, String newPassword, HttpSession session) throws ExceptionController{
        if (oldPassword == null || oldPassword.isEmpty()) throw new ExceptionController(400, "Old password not send!");
        if (newPassword == null || newPassword.isEmpty()) throw new ExceptionController(400, "New password not send!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        var restaurant = restaurantDAO.getRestaurantById(id);

        if (!restaurant.getPassword().equals(Cripto.md5(oldPassword))) throw new ExceptionController(412, "Senha incorreta!");
        if (oldPassword.equals(newPassword)) throw new ExceptionController(400, "Nova senha é a mesma que a antiga!");

        restaurant.setPassword(newPassword);

        restaurantDAO.save(restaurant);
    }

    public Restaurant addPhone(String phone, HttpSession session) throws ExceptionController{
        if (phone == null) throw new ExceptionController(400, "Phone not send!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);

        var newPhone = new RestaurantPhone(phone, restaurant);
        phoneDAO.save(newPhone);

        return restaurantDAO.getRestaurantById(id);
    }

    public Restaurant remPhone(String phone, HttpSession session) throws ExceptionController{
        if (phone == null) throw new ExceptionController(400, "Phone not send!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");

        var selectedPhone = phoneDAO.getRestaurantPhoneByPhone(phone);
        var restaurant = restaurantDAO.getRestaurantById(id);

        if (!selectedPhone.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(409, "Você não pode remover telefonão sejam seus!");

        phoneDAO.delete(selectedPhone);
        return restaurantDAO.getRestaurantById(id);
    }

    public Restaurant score(UUID idRestaurant, InScoreDTO dto, HttpSession session) throws ExceptionController{
        if (dto.score() == null) throw new ExceptionController(400, "Score value not sent!");
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("client")) throw new ExceptionController(401, "Você não está Logado em uma conta de Cliente!");

        var id = (UUID) session.getAttribute("user");

        if (!clientDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Cliente não cadastrado!");
        }

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");

        var client = clientDAO.getClientById(id);
        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        for (var score : restaurant.getScore()) if (score.getClient().getId().equals(client.getId())) {
            score.setScore(dto.score());
            scoreDAO.save(score);
            return restaurantDAO.getRestaurantById(restaurant.getId());
        }

        var score = new Score(dto.score(), client, restaurant);

        scoreDAO.save(score);

        return restaurantDAO.getRestaurantById(restaurant.getId());
    }

    public void delete(HttpSession session) throws ExceptionController{
        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");

        restaurantDAO.deleteById(id);

        if (restaurantDAO.existsById(id)) throw new ExceptionController(500, "Erro ao deletar Restaurante!");
    }
}
