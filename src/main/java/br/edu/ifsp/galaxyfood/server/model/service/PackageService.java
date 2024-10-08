package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Package;
import br.edu.ifsp.galaxyfood.server.model.dto.InPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.PackageDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.RestaurantDAO;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PackageService {

    private final RestaurantDAO restaurantDAO;

    private final PackageDAO packageDAO;

    public PackageService(@NonNull RestaurantDAO restaurantDAO, @NonNull PackageDAO packageDAO) {
        this.restaurantDAO = restaurantDAO;
        this.packageDAO = packageDAO;
    }

    public Package create(InPackageDTO dto, UUID userId) throws ExceptionController {

        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");

        if (!restaurantDAO.existsById(userId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        Package parent = null;

        if (dto.parent() == null && packageDAO.getRoot(userId) != null) {
            throw new ExceptionController(400, "Parent id not sent!");
        }

        if (dto.parent() != null && !packageDAO.existsById(dto.parent())) {
            throw new ExceptionController(404, "Pasta não encontrada!");
        }

        if (dto.parent() != null) parent = packageDAO.getPackageById(dto.parent());

        var restaurant = restaurantDAO.getRestaurantById(userId);

        return packageDAO.save(new Package(dto.name(), dto.image(), restaurant, parent));
    }

    public Package update(UUID idPackage, InPackageDTO dto, UUID userId) throws ExceptionController {

        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");

        if (!restaurantDAO.existsById(userId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(userId);
        var selectedPackage = packageDAO.getPackageById(idPackage);

        if (!selectedPackage.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode modificar pastas que não sejam suas!");

        selectedPackage.setName(dto.name());
        selectedPackage.setImage(dto.image());

        return packageDAO.save(selectedPackage);
    }

    public Package move(UUID idPackage, UUID idParent, UUID userId) throws ExceptionController {

        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (!restaurantDAO.existsById(userId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta movida não encontrada!");
        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta destino não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(userId);
        var selectedPackage = packageDAO.getPackageById(idPackage);
        var parent = packageDAO.getPackageById(idParent);

        if (!selectedPackage.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode mover pastas que não sejam suas!");
        if (!parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode mover pasta para pasta que não sejam suas!");

        selectedPackage.setParent(parent);

        return packageDAO.save(selectedPackage);
    }

    public Package get(UUID idPackage) throws ExceptionController {
        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");
        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta não encontrada!");
        return packageDAO.getPackageById(idPackage);
    }

    public Package getRoot(UUID idRestaurant) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant id not sent!");
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");
        return packageDAO.getRoot(idRestaurant);
    }

    public List<Package> getAll(UUID idRestaurant) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant id not sent!");
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");
        return packageDAO.getAllByRestaurant(idRestaurant);
    }

    public void delete(UUID idPackage, UUID userId) throws ExceptionController {

        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");

        if (!restaurantDAO.existsById(userId)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(userId);
        var selectedPackage = packageDAO.getPackageById(idPackage);

        if (!selectedPackage.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode deletar pastas que não sejam suas!");
        if (selectedPackage.getParent() == null) throw new ExceptionController(401, "Você não pode deletar a pasta principal!");

        var internalPackages = recursiveSeek(selectedPackage).reversed();

        packageDAO.deleteAll(internalPackages);
        packageDAO.delete(selectedPackage);

        if (packageDAO.existsById(selectedPackage.getId())) throw new ExceptionController(500, "Erro ao deletar Pasta!");
    }

    private List<Package> recursiveSeek(Package parent) throws ExceptionController {
        List<Package> list = new ArrayList<>();

        if (!parent.getItems().isEmpty()) throw new ExceptionController(401, "Você não pode deletar uma pasta com produtos dentro!");
        for (var pack : parent.getChildren()) {
            list.add(pack);
            if (!pack.getChildren().isEmpty()) list.addAll(recursiveSeek(pack));
        }

        return list;
    }
}



