package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import br.edu.ifsp.galaxyfood.server.model.domain.ComboItem;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboItemDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ComboService {

    private final RestaurantDAO restaurantDAO;

    private final PackageDAO packageDAO;

    private final FoodDAO foodDAO;

    private final ComboItemDAO itemDAO;

    private final ComboDAO comboDAO;

    private final BuyItemDAO buyItemDAO;

    public Combo create(UUID idRestaurant, InComboDTO dto) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.parent() == null) throw new ExceptionController(400, "Parent not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(dto.parent())) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        var parent = packageDAO.getPackageById(dto.parent());

        if (!parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar combos em uma pasta que não seja sua!");

        return comboDAO.save(new Combo(dto.name(), dto.price(), dto.image(), parent));
    }

    public Combo update(UUID idRestaurant, UUID idCombo, InComboDTO dto) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        combo.setName(dto.name());
        combo.setPrice(dto.price());
        combo.setImage(dto.image());

        return comboDAO.save(combo);

    }

    public Combo move(UUID idRestaurant, UUID idCombo, UUID idParent) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        var newParent = packageDAO.getPackageById(idParent);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");
        if (!newParent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar pastas que não sejam suas!");

        combo.setParent(newParent);
        return comboDAO.save(combo);
    }

    public Combo get(UUID idRestaurant, UUID idCombo) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not send!");
        if (idCombo == null) throw new ExceptionController(400, "Combo ID not send!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        final var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        final var combo = comboDAO.getComboById(idCombo);

        if (!restaurant.getId().equals(combo.getParent().getRestaurant().getId())) throw new ExceptionController(401, "Este combo não pertence a este Restaurante!");

        return combo;
    }

    public List<Combo> getAllByRestaurant(UUID idRestaurant) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        return comboDAO.getAllByRestaurant(idRestaurant);
    }

    public Combo addFood(UUID idRestaurant, InComboItemDTO dto) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (dto.combo() == null) throw new ExceptionController(400, "Combo id not sent!");
        if (dto.item() == null) throw new ExceptionController(400, "Food id not sent!");
        if (dto.quantity() == null) throw  new ExceptionController(400, "Quantity not sent!");


        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!comboDAO.existsById(dto.combo())) throw new ExceptionController(404, "Combo não encontrado!");
        if (!foodDAO.existsById(dto.item())) throw new ExceptionController(404, "Alimento não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        var combo = comboDAO.getComboById(dto.combo());
        var food = foodDAO.getFoodById(dto.item());

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

    public Combo remFood(UUID idRestaurant, UUID idItem) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idItem == null) throw new ExceptionController(400, "Combo Item id not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!itemDAO.existsById(idItem)) throw new ExceptionController(404, "Alimento do combo não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        var item = itemDAO.getComboItemById(idItem);

        if (!item.getCombo().getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode remover alimentos de combos que não sejam seus!");

        var combo = comboDAO.getComboById(item.getCombo().getId());
        combo.getItems().remove(item);
        combo = comboDAO.save(combo);

        itemDAO.delete(item);
        return combo;
    }

    public void delete(UUID idRestaurant, UUID idCombo) throws ExceptionController{
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idCombo == null) throw new ExceptionController(400, "Combo id not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var combo = comboDAO.getComboById(idCombo);

        if (!combo.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar combos que não sejam seus!");

        for (var item : combo.getItems()){
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

        if (comboDAO.existsById(idCombo)) throw new ExceptionController(500, "Erro ao deletar Combo!");
    }
}
