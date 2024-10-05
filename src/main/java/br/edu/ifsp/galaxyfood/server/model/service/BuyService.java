package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import br.edu.ifsp.galaxyfood.server.model.domain.BuyItem;
import br.edu.ifsp.galaxyfood.server.model.domain.OrderStatus;
import br.edu.ifsp.galaxyfood.server.model.dto.InBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.repository.*;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BuyService {

    private final BuyDAO buyDAO;

    private final PackageItemDAO packageItemDAO;

    private final ClientDAO clientDAO;

    private final RestaurantDAO restaurantDAO;

    private final AddressDAO addressDAO;

    private final BuyItemDAO itemDAO;

    public Buy create(UUID idClient, InBuyDTO dto) throws ExceptionController {
        if (idClient == null) throw new ExceptionController(400, "Client ID not sent!");
        if (dto.paymentForm() == null) throw new ExceptionController(400, "Payment form not sent!");
        if (dto.sentAddress() == null) throw new ExceptionController(400, "Address ID not sent!");
        if (dto.restaurant() == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (dto.items() == null|| dto.items().isEmpty()) throw new ExceptionController(400, "Bought Items not sent!");

        BigDecimal deliveryFee;
        if (dto.deliveryFee() == null) deliveryFee = BigDecimal.ZERO;
        else deliveryFee = dto.deliveryFee();

        BigDecimal discount;
        if (dto.discount() == null) discount = BigDecimal.ZERO;
        else discount = dto.discount();

        if (!clientDAO.existsById(idClient)) throw new ExceptionController(412, "Cliente não cadastrado!");

        if (!clientDAO.existsById(idClient)) throw new ExceptionController(404, "Cliente não encontrado!");
        if (!restaurantDAO.existsById(dto.restaurant())) throw new ExceptionController(404, "Restaurante não encontrado!");
        if (!addressDAO.existsById(dto.sentAddress())) throw new ExceptionController(404, "Endereço não encontrado!");

        var client = clientDAO.getClientById(idClient);
        var restaurant = restaurantDAO.getRestaurantById(dto.restaurant());
        var address = addressDAO.getAddressById(dto.sentAddress());
        var date = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        var buy = buyDAO.save(new Buy(dto.paymentForm(), OrderStatus.WAITING, date, deliveryFee, discount, address, client, restaurant));

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

    public Buy get(UUID idUser, UUID idBuy, String typeUser){

        if (idBuy == null) throw new ExceptionController(400, "Buy id not sent!");

        if (!buyDAO.existsById(idBuy)) throw new ExceptionController(404, "Compra não econtrada!");

        var buy = buyDAO.getBuyById(idBuy);

        if (typeUser.equalsIgnoreCase("client")) {

            if (!clientDAO.existsById(idUser)) throw new ExceptionController(412, "Cliente não cadastrado!");

            var client = clientDAO.getClientById(idUser);

            if (!buy.getClient().getId().equals(client.getId())) throw new ExceptionController(401, "Você não pode acessar compras que não sejam suas!");

        } else if (typeUser.equalsIgnoreCase("restaurant")){

            if (!restaurantDAO.existsById(idUser)) throw new ExceptionController(412, "Restaurante não cadastrado!");

            var restaurant = restaurantDAO.getRestaurantById(idUser);

            if (!buy.getRestaurant().getId().equals(restaurant.getId())) throw new ExceptionController(401, "Você não pode acessar compras que tenham seus produtos!");
        }

        return buy;
    }

    public List<Buy> getAll(UUID idUser, String typeUser) {

        List<Buy> buys = new ArrayList<>();

        if (typeUser.equalsIgnoreCase("client")) {

            if (!clientDAO.existsById(idUser)) throw new ExceptionController(412, "Cliente não cadastrado!");

            buys = buyDAO.getAllByClient(idUser);

        } else if (typeUser.equalsIgnoreCase("restaurant")){

            if (!restaurantDAO.existsById(idUser)) throw new ExceptionController(412, "Restaurante não cadastrado!");

            buys = buyDAO.getAllByRestaurant(idUser);
        }

        return buys;
    }

    public Buy updateOrderStatus(UUID idRestaurant, UUID idBuy, OrderStatus status){
        if (idRestaurant == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idBuy == null) throw new ExceptionController(400, "Buy ID not sent!");
        if (status == null) throw new ExceptionController(400, "Order Status not sent!");

        if (!restaurantDAO.existsById(idRestaurant)) throw new ExceptionController(412, "Restaurante não cadastrado!");

        if (!buyDAO.existsById(idBuy)) throw new ExceptionController(404, "Compra não econtrada!");

        var buy = buyDAO.getBuyById(idBuy);

        buy.setOrderStatus(status);
        return buyDAO.save(buy);
    }

    public Buy cancel(UUID idClient, UUID idBuy){
        if (idClient == null) throw new ExceptionController(400, "Restaurant ID not sent!");
        if (idBuy == null) throw new ExceptionController(400, "Buy ID not sent!");

        if (!clientDAO.existsById(idClient)) throw new ExceptionController(412, "Cliente não cadastrado!");

        if (!buyDAO.existsById(idBuy)) throw new ExceptionController(404, "Compra não econtrada!");

        var buy = buyDAO.getBuyById(idBuy);

        buy.setOrderStatus(OrderStatus.CANCELED);
        return buyDAO.save(buy);
    }
}
