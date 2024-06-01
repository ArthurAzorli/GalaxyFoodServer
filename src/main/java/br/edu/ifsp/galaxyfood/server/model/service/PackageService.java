package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Package;
import br.edu.ifsp.galaxyfood.server.model.dto.InPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.PackageDAO;
import br.edu.ifsp.galaxyfood.server.model.repository.RestaurantDAO;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Service;

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

    public Package create (InPackageDTO dto, HttpSession session) throws ExceptionController {

        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");

        Package parent = null;

        if (dto.parent() == null && packageDAO.getRoot(id) != null) throw new ExceptionController(400, "Parent id not sent!");
        if (dto.parent() != null && !packageDAO.existsById(dto.parent())) throw new ExceptionController(404, "Pasta não encontrada!");
        if (dto.parent() != null) parent = packageDAO.getPackageById(dto.parent());

        var restaurant = restaurantDAO.getRestaurantById(id);

        return packageDAO.save(new Package(dto.name(), dto.image(), restaurant, parent));
    }

    public Package update (UUID idPackage, InPackageDTO dto, HttpSession session) throws ExceptionController {

        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");
        if (dto.name() == null) throw new ExceptionController(400, "Name not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var selectedPackage = packageDAO.getPackageById(idPackage);

        if (!selectedPackage.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode modificar pastas que não sejam suas!");

        selectedPackage.setName(dto.name());
        selectedPackage.setImage(dto.image());

        return packageDAO.save(selectedPackage);
    }

    public Package move (UUID idPackage, UUID idParent, HttpSession session) throws ExceptionController {

        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");
        if (idParent == null) throw new ExceptionController(400, "Parent id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta movida não encontrada!");
        if (!packageDAO.existsById(idParent)) throw new ExceptionController(404, "Pasta destino não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var selectedPackage = packageDAO.getPackageById(idPackage);
        var parent = packageDAO.getPackageById(idParent);

        if (!selectedPackage.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode mover pastas que não sejam suas!");
        if (!parent.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode mover pasta para pasta que não sejam suas!");

        selectedPackage.setParent(parent);

        return packageDAO.save(selectedPackage);
    }

    public Package get (UUID idPackage) throws ExceptionController {
        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");
        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta não encontrada!");
        return packageDAO.getPackageById(idPackage);
    }

    public Package getRoot (UUID idRestaurant) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant id not sent!");
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");
        return packageDAO.getRoot(idRestaurant);
    }

    public Package getRoot (HttpSession session) throws ExceptionController {
        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        return getRoot(id);
    }

    public List<Package> getAll(UUID idRestaurant) throws ExceptionController {
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant id not sent!");
        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(404, "Restaurante não encontrado!");
        return packageDAO.getAllByRestaurant(idRestaurant);
    }

    public List<Package> getAll(HttpSession session) throws ExceptionController {
        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        return getAll(id);
    }

    public void delete(UUID idPackage, HttpSession session) throws ExceptionController {

        if (idPackage == null) throw new ExceptionController(400, "Package id not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("restaurant")) throw new ExceptionController(401, "Você não está Logado em uma conta de Restaurante!");

        var id = (UUID) session.getAttribute("user");

        if (!restaurantDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!restaurantDAO.existsById(id)) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!packageDAO.existsById(idPackage)) throw new ExceptionController(404, "Pasta não encontrada!");

        var restaurant = restaurantDAO.getRestaurantById(id);
        var selectedPackage = packageDAO.getPackageById(idPackage);

        if (!selectedPackage.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode deletar pastas que não sejam suas!");

        packageDAO.delete(selectedPackage);

        if (packageDAO.existsById(selectedPackage.getId())) throw new ExceptionController(500, "Erro ao deletar Pasta!");
    }
}
