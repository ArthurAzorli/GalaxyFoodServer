package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import br.edu.ifsp.galaxyfood.server.model.domain.ComboItem;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboItemDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ComboService {

    private final RestaurantDAO restaurantDAO;

    private final PackageDAO packageDAO;

    private final FoodDAO foodDAO;

    private final ComboItemDAO itemDAO;

    private final ComboDAO comboDAO;

    public ComboService(@NonNull RestaurantDAO restaurantDAO, @NonNull PackageDAO packageDAO, @NonNull FoodDAO foodDAO, @NonNull ComboItemDAO itemDAO,  @NonNull ComboDAO comboDAO) {
        this.restaurantDAO = restaurantDAO;
        this.packageDAO = packageDAO;
        this.foodDAO = foodDAO;
        this.itemDAO = itemDAO;
        this.comboDAO = comboDAO;
    }

    public Combo create(InComboDTO dto, HttpSession session) throws ExceptionController {

        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.parent() == null) throw new ExceptionController(400, "Parent not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!packageDAO.existsById(dto.parent())) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var parent = packageDAO.getPackageById(dto.parent());

        if (!parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar combos em uma pasta que não seja sua!");

        return comboDAO.save(new Combo(dto.name(), dto.price(), dto.image(), parent));
    }

    public Combo update(UUID idCombo, InComboDTO dto, HttpSession session) throws ExceptionController {

        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        var restaurant = restaurantDAO.getRestaurantById(id);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        combo.setName(dto.name());
        combo.setPrice(dto.price());
        combo.setImage(dto.image());

        return comboDAO.save(combo);

    }

    public Combo move(UUID idCombo, UUID idParent, HttpSession session) throws ExceptionController {
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var newParent = packageDAO.getPackageById(idParent);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");
        if (!newParent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar pastas que não sejam suas!");

        combo.setParent(newParent);
        return comboDAO.save(combo);
    }

    public Combo get(UUID idCombo) throws ExceptionController {
        if (idCombo == null) throw new ExceptionController(400, "Combo id not send!");
        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");
        return comboDAO.getComboById(idCombo);
    }

    public List<Combo> getAll(HttpSession session) throws ExceptionController {

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        return comboDAO.getAllByRestaurant(id);
    }

    public List<Combo> getAll(UUID idRestaurant, HttpSession session) throws ExceptionController {
        if (idRestaurant == null) return getAll(session);
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        return comboDAO.getAllByRestaurant(idRestaurant);
    }

    public Combo addFood(InComboItemDTO dto, HttpSession session) throws ExceptionController {
        if (dto.combo() == null) throw new ExceptionController(400, "Combo id not sent!");
        if (dto.food() == null) throw new ExceptionController(400, "Food id not sent!");
        if (dto.quantity() == null) throw  new ExceptionController(400, "Quantity not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!comboDAO.existsById(dto.combo())) throw new ExceptionController(404, "Combo não encontrado!");
        if (!foodDAO.existsById(dto.food())) throw new ExceptionController(404, "Alimento não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var combo = comboDAO.getComboById(dto.combo());
        var food = foodDAO.getFoodById(dto.food());

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar alimentos a combos que não sejam seus!");
        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar alimentos que não sejam seus!");
        if (dto.quantity()<=0) throw new ExceptionController(406, "Quantidade inválida!");

        if (itemDAO.existsByParams(combo, food)){
            var item = itemDAO.getComboItemByParams(combo, food);
            item.setQuantity(dto.quantity());
            itemDAO.save(item);
        } else {
            var item = itemDAO.save(new ComboItem(dto.quantity(), food, combo));
            combo.addItem(item);
            comboDAO.save(combo);
        }

        return comboDAO.getComboById(dto.combo());
    }

    public Combo remFood(UUID idItem, HttpSession session) throws ExceptionController {

        if (idItem == null) throw new ExceptionController(400, "Combo Item id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!itemDAO.existsById(idItem)) throw new ExceptionController(404, "Alimento do combo não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var item = itemDAO.getComboItemById(idItem);

        if (!item.getCombo().getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode remover alimentos de combos que não sejam seus!");

        var combo = comboDAO.getComboById(item.getCombo().getId());
        combo.getItems().remove(item);
        combo = comboDAO.save(combo);

        itemDAO.delete(item);
        return combo;
    }

    public void delete(UUID idCombo, HttpSession session) throws ExceptionController{
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        var restaurant = restaurantDAO.getRestaurantById(id);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar combos que não sejam seus!");

        itemDAO.deleteAll(combo.getItems());
        comboDAO.delete(combo);

        if (comboDAO.existsById(idCombo)) throw new ExceptionController(500, "Erro ao deletar Combo!");
    }
}
