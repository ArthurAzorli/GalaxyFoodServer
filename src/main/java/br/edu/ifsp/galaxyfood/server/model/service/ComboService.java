package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InFoodDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.ComboDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.FoodDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.PackageDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.RestaurantDAO;
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

    private final ComboDAO comboDAO;

    public ComboService(@NonNull RestaurantDAO restaurantDAO, @NonNull PackageDAO packageDAO, @NonNull FoodDAO foodDAO, ComboDAO comboDAO) {
        this.restaurantDAO = restaurantDAO;
        this.packageDAO = packageDAO;
        this.foodDAO = foodDAO;
        this.comboDAO = comboDAO;
    }

    public Combo create(InComboDTO dto, HttpSession session) throws ExceptionController {
        if (dto.name() == null) throw new ExceptionController(400, "Name not send!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.parent() == null) throw new ExceptionController(400, "Parent not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!packageDAO.existsById(dto.parent())) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var parent = packageDAO.getPackageById(dto.parent());

        if (parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar combos em uma pasta que não seja sua!");

        return comboDAO.save(new Combo(dto.name(), dto.price(), dto.image(), parent));
    }

    public Food update(UUID idCombo, InComboDTO dto, HttpSession session) throws ExceptionController {

        if (dto.name() == null) throw new ExceptionController(400, "Name not send!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");

        var restaurant = restaurantDAO.getRestaurantById(id);

        if (!comboDAO.existsById(idCombo)) throw new ExceptionController(404, "Combo não encontrado!");

        var food = foodDAO.getFoodById(idCombo);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        food.setName(dto.name());
        food.setPrice(dto.price());
        food.setImage(dto.image());

        return foodDAO.save(food);

    }

    public Food move(UUID idFood, UUID idParent, HttpSession session) throws ExceptionController {
        if (idFood == null) throw new ExceptionController(400, "Food's id not send!");
        if (idParent == null) throw new ExceptionController(400, "Parent's id not send!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var newParent = packageDAO.getPackageById(idParent);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");
        if (!newParent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar pastas que não sejam suas!");

        food.setParent(newParent);
        return foodDAO.save(food);
    }

    public Food get(UUID idFood) throws ExceptionController {
        if (idFood == null) throw new ExceptionController(400, "Food's id not send!");
        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");
        return foodDAO.getFoodById(idFood);
    }

    public List<Food> getAll(HttpSession session) {

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        return foodDAO.getAllByRestaurant(id);
    }

    public List<Food> getAll(UUID idRestaurant, HttpSession session) {
        if (idRestaurant == null) return getAll(session);
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        return foodDAO.getAllByRestaurant(idRestaurant);
    }
}
