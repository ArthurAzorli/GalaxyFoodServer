package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import br.edu.ifsp.galaxyfood.server.model.domain.BuyItem;
import br.edu.ifsp.galaxyfood.server.model.dto.InBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BuyService {

    private final BuyDAO buyDAO;

    private final PackageItemDAO packageItemDAO;

    private final ClientDAO clientDAO;

    private final RestaurantDAO restaurantDAO;

    private final AddressDAO addressDAO;

    private final BuyItemDAO itemDAO;

    public BuyService(@NonNull BuyDAO buyDAO, @NonNull PackageItemDAO packageItemDAO, @NonNull ClientDAO clientDAO, @NonNull RestaurantDAO restaurantDAO, @NonNull AddressDAO addressDAO, @NonNull BuyItemDAO itemDAO) {
        this.buyDAO = buyDAO;
        this.packageItemDAO = packageItemDAO;
        this.clientDAO = clientDAO;
        this.restaurantDAO = restaurantDAO;
        this.addressDAO = addressDAO;
        this.itemDAO = itemDAO;
    }

    public Buy create(InBuyDTO dto, HttpSession session) throws ExceptionController {

        if (dto.paymentForm() == null) throw new ExceptionController(400, "Payment form not sent!");
        if (dto.sentAddress() == null) throw new ExceptionController(400, "Address id not sent!");
        if (dto.restaurant() == null) throw new ExceptionController(400, "Restaurant id not sent!");
        if (dto.items() == null|| dto.items().isEmpty()) throw new ExceptionController(400, "Bought Items not sent!");

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (!session.getAttribute("type").equals("client")) throw new ExceptionController(401, "Você não está Logado em uma conta de Cliente!");

        var id = (UUID) session.getAttribute("user");

        if (!clientDAO.existsById(id)) {
            session.removeAttribute("user");
            throw new ExceptionController(412, "Cliente não cadastrado!");
        }

        if (!clientDAO.existsById(id)) throw new ExceptionController(404, "Cliente não encontrado!");
        if (!restaurantDAO.existsById(dto.restaurant())) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!addressDAO.existsById(dto.sentAddress())) throw new ExceptionController(404, "Endereço não encontrado!");

        var client = clientDAO.getClientById(id);
        var restaurant = restaurantDAO.getRestaurantById(dto.restaurant());
        var address = addressDAO.getAddressById(dto.sentAddress());
        var date = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        var buy = buyDAO.save(new Buy(dto.paymentForm(), date, address, client, restaurant));

        List<BuyItem> buyItems = new ArrayList<>();
        for (var itemDTO : dto.items()){
            if (itemDTO.idItem() == null) throw new ExceptionController(400, "Package item id not sent!");
            if (itemDTO.quantity() == null) throw new ExceptionController(400, "Quantity not sent!");

            if (!packageItemDAO.existsById(itemDTO.idItem())) throw new ExceptionController(404, "Produto não encontrado!");
            if (itemDTO.quantity() <= 0) throw new ExceptionController(406, "Quantidade inválida!");

            var item = packageItemDAO.getPackageItemById(itemDTO.idItem());

            var buyItem = itemDAO.save(new BuyItem(buy, item, itemDTO.quantity()));

            buyItems.add(buyItem);
        }

        buy.setItems(buyItems);

        return buyDAO.save(buy);

    }

    public Buy get(UUID idBuy, HttpSession session){

        if (idBuy == null) throw new ExceptionController(400, "Buy id not sent!");

        if (!buyDAO.existsById(idBuy)) throw new ExceptionController(404, "Compra não econtrada!");

        var buy = buyDAO.getBuyById(idBuy);

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("client")) {

            var id = (UUID) session.getAttribute("user");

            if (!clientDAO.existsById(id)) {
                session.removeAttribute("user");
                throw new ExceptionController(412, "Cliente não cadastrado!");
            }

            var client = clientDAO.getClientById(id);

            if (!buy.getClient().getId().equals(client.getId())) throw new ExceptionController(401, "Você não pode acessar compras que não sejam suas!");

        } else if (session.getAttribute("type").equals("restaurant")){

            var id = (UUID) session.getAttribute("user");

            if (!restaurantDAO.existsById(id)) {
                session.removeAttribute("user");
                throw new ExceptionController(412, "Restaurante não cadastrado!");
            }

            var restaurant = restaurantDAO.getRestaurantById(id);

            if (!buy.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode acessar compras que tenham seus produtos!");
        }

        return buy;
    }

    public List<Buy> getAll(HttpSession session) {

        List<Buy> buys = new ArrayList<>();

        if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está Logado!");
        if (session.getAttribute("type").equals("client")) {

            var id = (UUID) session.getAttribute("user");

            if (!clientDAO.existsById(id)) {
                session.removeAttribute("user");
                throw new ExceptionController(412, "Cliente não cadastrado!");
            }

            buys = buyDAO.getAllByClient(id);

        } else if (session.getAttribute("type").equals("restaurant")){

            var id = (UUID) session.getAttribute("user");

            if (!restaurantDAO.existsById(id)) {
                session.removeAttribute("user");
                throw new ExceptionController(412, "Restaurante não cadastrado!");
            }

            buys = buyDAO.getAllByRestaurant(id);
        }

        return buys;
    }
}
