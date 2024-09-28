package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import br.edu.ifsp.galaxyfood.server.model.domain.BuyItem;
import br.edu.ifsp.galaxyfood.server.model.domain.OrderStatus;
import br.edu.ifsp.galaxyfood.server.model.dto.InBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Buy create(InBuyDTO dto, UUID clientId) throws ExceptionController {

        if (dto.paymentForm() == null) throw new ExceptionController(400, "Payment form not sent!");
        if (dto.sentAddress() == null) throw new ExceptionController(400, "Address id not sent!");
        if (dto.restaurant() == null) throw new ExceptionController(400, "Restaurant id not sent!");
        if (dto.items() == null || dto.items().isEmpty()) throw new ExceptionController(400, "Bought Items not sent!");
        BigDecimal deliveryFee = dto.deliveryFee() != null ? dto.deliveryFee() : BigDecimal.ZERO;
        BigDecimal discount = dto.discount() != null ? dto.discount() : BigDecimal.ZERO;
        if (!clientDAO.existsById(clientId)) throw new ExceptionController(412, "Cliente não cadastrado!");
        if (!restaurantDAO.existsById(dto.restaurant())) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!addressDAO.existsById(dto.sentAddress())) throw new ExceptionController(404, "Endereço não encontrado!");

        var client = clientDAO.getClientById(clientId);
        var restaurant = restaurantDAO.getRestaurantById(dto.restaurant());
        var address = addressDAO.getAddressById(dto.sentAddress());
        var date = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        var buy = buyDAO.save(new Buy(dto.paymentForm(), OrderStatus.WAITING, date, deliveryFee, discount, address, client, restaurant));

        List<BuyItem> buyItems = new ArrayList<>();
        for (var itemDTO : dto.items()) {
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

    public Buy get(UUID idBuy, UUID userId, String userType) {

        if (idBuy == null) throw new ExceptionController(400, "Buy id not sent!");

        if (!buyDAO.existsById(idBuy)) throw new ExceptionController(404, "Compra não encontrada!");

        var buy = buyDAO.getBuyById(idBuy);

        if ("client".equals(userType)) {
            if (!clientDAO.existsById(userId)) {
                throw new ExceptionController(412, "Cliente não cadastrado!");
            }

            var client = clientDAO.getClientById(userId);

            if (!buy.getClient().getId().equals(client.getId())) throw new ExceptionController(401, "Você não pode acessar compras que não sejam suas!");

        } else if ("restaurant".equals(userType)) {

            if (!restaurantDAO.existsById(userId)) {
                throw new ExceptionController(412, "Restaurante não cadastrado!");
            }

            var restaurant = restaurantDAO.getRestaurantById(userId);

            if (!buy.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode acessar compras que não sejam do seu restaurante!");
        }

        return buy;
    }
    public List<Buy> getAll(UUID userId, String userType) {

        List<Buy> buys = new ArrayList<>();

        if (userType.equals("client")) {
            if (!clientDAO.existsById(userId)) throw new ExceptionController(412, "Cliente não cadastrado!");
            buys = buyDAO.getAllByClient(userId);

        } else if (userType.equals("restaurant")) {
            if (!restaurantDAO.existsById(userId)) throw new ExceptionController(412, "Restaurante não cadastrado!");
            buys = buyDAO.getAllByRestaurant(userId);
        }

        return buys;
    }

    public Buy updateOrderStatus(UUID idBuy, OrderStatus status, UUID restaurantId) {
        if (idBuy == null) throw new ExceptionController(400, "Buy id not sent!");
        if (status == null) throw new ExceptionController(400, "Order Status not sent!");

        if (!restaurantDAO.existsById(restaurantId)) {
            throw new ExceptionController(412, "Restaurante não cadastrado!");
        }

        if (!buyDAO.existsById(idBuy)) throw new ExceptionController(404, "Compra não encontrada!");

        var buy = buyDAO.getBuyById(idBuy);

        buy.setOrderStatus(status);
        return buyDAO.save(buy);
    }
}

