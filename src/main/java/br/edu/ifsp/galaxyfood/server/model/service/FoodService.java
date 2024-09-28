package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import br.edu.ifsp.galaxyfood.server.model.dto.InFoodDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.BuyItemDAO;
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
public class FoodService {

    private final RestaurantDAO restaurantDAO;

    private final PackageDAO packageDAO;

    private final BuyItemDAO itemDAO;

    private final FoodDAO foodDAO;

    public FoodService(@NonNull RestaurantDAO restaurantDAO, @NonNull PackageDAO packageDAO, @NonNull BuyItemDAO itemDAO, @NonNull FoodDAO foodDAO) {
        this.restaurantDAO = restaurantDAO;
        this.packageDAO = packageDAO;
        this.itemDAO = itemDAO;
        this.foodDAO = foodDAO;
    }

    public Food create(InFoodDTO dto, UUID restaurantId) throws ExceptionController {
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.description() == null) throw new ExceptionController(400, "Description not sent!");
        if (dto.parent() == null) throw new ExceptionController(400, "Parent not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        if (!packageDAO.existsById(dto.parent())) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(restaurantId);
        var parent = packageDAO.getPackageById(dto.parent());

        if (!parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar alimentos em uma pasta que não seja sua!");

        return foodDAO.save(new Food(dto.name(), dto.price(), dto.description(), dto.image(), parent));
    }


    public Food update(UUID idFood, InFoodDTO dto, UUID restaurantId) throws ExceptionController {
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.description() == null) throw new ExceptionController(400, "Description not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        var restaurant = restaurantDAO.getRestaurantById(restaurantId);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        food.setName(dto.name());
        food.setPrice(dto.price());
        food.setDescription(dto.description());
        food.setImage(dto.image());

        return foodDAO.save(food);
    }

    public Food move(UUID idFood, UUID idParent, UUID restaurantId) throws ExceptionController {
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(restaurantId);
        var newParent = packageDAO.getPackageById(idParent);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");
        if (!newParent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar pastas que não sejam suas!");

        food.setParent(newParent);
        return foodDAO.save(food);
    }

    public Food get(UUID idFood) throws ExceptionController {
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");
        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");
        return foodDAO.getFoodById(idFood);
    }

    public List<Food> getAll(UUID restaurantId) throws ExceptionController {
        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        return foodDAO.getAllByRestaurant(restaurantId);
    }

    public void delete(UUID idFood, UUID restaurantId) throws ExceptionController {
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");

        if (!restaurantDAO.existsById(restaurantId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(restaurantId);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId()))
            throw new ExceptionController(401, "Você não pode deletar alimentos que não sejam seus!");

        for (var item : itemDAO.getAllByPackageItem(idFood)) {
            item.setItem(null);
            itemDAO.save(item);
        }

        foodDAO.delete(food);

        if (foodDAO.existsById(idFood)) throw new ExceptionController(500, "Erro ao deletar Alimento!");
    }
}
