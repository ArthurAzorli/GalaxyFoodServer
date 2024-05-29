package br.edu.ifsp.galaxyfood.server.model.service;

import br.edu.ifsp.galaxyfood.server.model.repository.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class BuyService {

    private final BuyDAO buyDAO;

    private final BuyItemDAO buyItemDAO;

    private final PackageItemDAO packageItemDAO;

    private final ClientDAO clientDAO;

    private final RestaurantDAO restaurantDAO;

    public BuyService(@NonNull BuyDAO buyDAO, @NonNull BuyItemDAO buyItemDAO, @NonNull PackageItemDAO packageItemDAO, @NonNull ClientDAO clientDAO, @NonNull RestaurantDAO restaurantDAO) {
        this.buyDAO = buyDAO;
        this.buyItemDAO = buyItemDAO;
        this.packageItemDAO = packageItemDAO;
        this.clientDAO = clientDAO;
        this.restaurantDAO = restaurantDAO;
    }

}
