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

    private final BuyItemDAO buyItemDAO;

    public ComboService(@NonNull RestaurantDAO restaurantDAO, @NonNull PackageDAO packageDAO, @NonNull FoodDAO foodDAO, @NonNull ComboItemDAO itemDAO, @NonNull ComboDAO comboDAO, @NonNull BuyItemDAO buyItemDAO) {
        this.restaurantDAO = restaurantDAO;
        this.packageDAO = packageDAO;
        this.foodDAO = foodDAO;
        this.itemDAO = itemDAO;
        this.comboDAO = comboDAO;
        this.buyItemDAO = buyItemDAO;
    }

    public Combo create(InComboDTO dto) throws ExceptionController {
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.parent() == null) throw new ExceptionController(400, "Parent not sent!");
        var parent = packageDAO.getPackageById(dto.parent());

        if (parent == null) throw new ExceptionController(404, "Pasta não encontrada!");
        var restaurant = parent.getRestaurant();
        if (restaurant == null) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var combo = new Combo(dto.name(), dto.price(), dto.image(), parent);
        return comboDAO.save(combo);
    }

    public Combo update(UUID idCombo, InComboDTO dto, UUID restaurantId) throws ExceptionController {
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(restaurantId);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        combo.setName(dto.name());
        combo.setPrice(dto.price());
        combo.setImage(dto.image());

        return comboDAO.save(combo);
    }

    public Combo move(UUID idCombo, UUID idParent, UUID restaurantId) throws ExceptionController {
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (!restaurantDAO.existsById(restaurantId)) {
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(restaurantId);
        var newParent = packageDAO.getPackageById(idParent);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");
        if (!newParent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar pastas que não sejam suas!");

        combo.setParent(newParent);
        return comboDAO.save(combo);
    }

    public Combo get(UUID idCombo) throws ExceptionController {
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");
        return comboDAO.getComboById(idCombo);
    }

    public List<Combo> getAll(UUID restaurantId) throws ExceptionController {
        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        return comboDAO.getAllByRestaurant(restaurantId);
    }

    public Combo addFood(InComboItemDTO dto, UUID restaurantId) throws ExceptionController {
        if (dto.combo() == null) throw new ExceptionController(400, "Combo id not sent!");
        if (dto.food() == null) throw new ExceptionController(400, "Food id not sent!");
        if (dto.quantity() == null) throw new ExceptionController(400, "Quantity not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");


        if (!comboDAO.existsById(dto.combo())) throw new ExceptionController(404, "Combo não encontrado!");
        if (!foodDAO.existsById(dto.food())) throw new ExceptionController(404, "Alimento não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(restaurantId);
        var combo = comboDAO.getComboById(dto.combo());
        var food = foodDAO.getFoodById(dto.food());

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar alimentos a combos que não sejam seus!");
        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar alimentos que não sejam seus!");
        if (dto.quantity() <= 0) throw new ExceptionController(406, "Quantidade inválida!");

        if (itemDAO.existsByParams(combo, food)) {
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
    public Combo remFood(UUID idItem, UUID restaurantId) throws ExceptionController {
        if (idItem == null) throw new ExceptionController(400, "Combo Item id not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        if (!itemDAO.existsById(idItem)) throw new ExceptionController(404, "Alimento do combo não encontrado!");
        var restaurant = restaurantDAO.getRestaurantById(restaurantId);
        var item = itemDAO.getComboItemById(idItem);
        if (!item.getCombo().getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode remover alimentos de combos que não sejam seus!");
        var combo = comboDAO.getComboById(item.getCombo().getId());
        combo.getItems().remove(item);
        combo = comboDAO.save(combo);

        itemDAO.delete(item);
        return combo;
    }

    public void delete(UUID idCombo, UUID restaurantId) throws ExceptionController {
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        var restaurant = restaurantDAO.getRestaurantById(restaurantId);
        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");
        var combo = comboDAO.getComboById(idCombo);
        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar combos que não sejam seus!");

        for (var item : combo.getItems()) {
            item.setItem(null);
            item.setCombo(null);
            itemDAO.save(item);
            itemDAO.delete(item);
        }
        for (var item : buyItemDAO.getAllByPackageItem(idCombo)) {
            item.setItem(null);
            buyItemDAO.save(item);
        }
        itemDAO.deleteAll(combo.getItems());
        comboDAO.delete(combo);

        if (comboDAO.existsById(idCombo)) throw new ExceptionController(500, "Erro ao deletar Combo");
    }
}
