package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Food;
import br.edu.ifsp.galaxyfood.server.model.dto.InFoodDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.BuyItemDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.FoodDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.PackageDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.RestaurantDAO;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FoodService {

    private final RestaurantDAO restaurantDAO;

    private final PackageDAO packageDAO;

    private final BuyItemDAO itemDAO;

    private final FoodDAO foodDAO;

    public Food create(UUID idRestaurant, InFoodDTO dto) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.description() == null) throw new ExceptionController(400, "Description not sent!");
        if (dto.parent() == null) throw new ExceptionController(400, "Parent not sent!");


        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(dto.parent())) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        var parent = packageDAO.getPackageById(dto.parent());

        if (!parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode adicionar alimentos em uma pasta que não seja sua!");

        return foodDAO.save(new Food(dto.name(), dto.price(), dto.description(), dto.image(), parent));
    }

    public Food update(UUID idRestaurant, UUID idFood, InFoodDTO dto) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");
        if (dto.price() == null) throw new ExceptionController(400, "Price not sent!");
        if (dto.image() == null) throw new ExceptionController(400, "Image not sent!");
        if (dto.description() == null) throw new ExceptionController(400, "Description not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        food.setName(dto.name());
        food.setPrice(dto.price());
        food.setDescription(dto.description());
        food.setImage(dto.image());

        return foodDAO.save(food);

    }

    public Food move(UUID idRestaurant, UUID idFood, UUID idParent) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        var newParent = packageDAO.getPackageById(idParent);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");
        if (!newParent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar pastas que não sejam suas!");

        food.setParent(newParent);
        return foodDAO.save(food);
    }

    public Food get(UUID idRestaurant, UUID idFood) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not send!");
        if (idFood == null) throw new ExceptionController(400, "Food ID not send!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        final var restaurant = restaurantDAO.getRestaurantById(idRestaurant);
        final var food = foodDAO.getFoodById(idFood);

        if (!restaurant.getId().equals(food.getParent().getRestaurant().getId())) throw new ExceptionController(401, "Este alimento não pertence a este Restaurante!");

        return food;
    }

    public List<Food> getAllByRestaurant(UUID idRestaurant) {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");
        return foodDAO.getAllByRestaurant(idRestaurant);
    }

    public void delete(UUID idRestaurant, UUID idFood) throws ExceptionController{
        if (idFood == null) throw new ExceptionController(400, "Food id not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        var restaurant = restaurantDAO.getRestaurantById(idRestaurant);

        if (!foodDAO.existsById(idFood)) throw new ExceptionController(404, "Alimento não encontrado!");

        var food = foodDAO.getFoodById(idFood);

        if (!food.getParent().getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode alterar alimentos que não sejam seus!");

        for (var item : itemDAO.getAllByPackageItem(idFood)) {
            item.setItem(null);
            itemDAO.save(item);
        }

        foodDAO.delete(food);

        if (foodDAO.existsById(idFood)) throw new ExceptionController(500, "Erro ao deletar Alimento!");
    }
    
}
