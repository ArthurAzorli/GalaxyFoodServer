package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.Buy;
import br.edu.ifsp.galaxyfood.server.model.domain.BuyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BuyItemDAO extends JpaRepository<BuyItem, UUID> {

    @Query(value = "SELECT b FROM BuyItem b WHERE b.item.id = ?1")
    List<BuyItem> getAllByPackageItem(UUID idFood);
}
