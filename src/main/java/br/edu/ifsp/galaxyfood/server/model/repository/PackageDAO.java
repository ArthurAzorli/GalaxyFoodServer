package br.edu.ifsp.galaxyfood.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.galaxyfood.server.model.domain.Package;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PackageDAO extends JpaRepository<Package, UUID> {

    @Query(value = "SELECT p FROM Package p WHERE p.id = ?1")
    Package getPackageById(UUID id);

    @Query(value = "SELECT p FROM Package p WHERE p.restaurant.id = ?1 AND p.parent IS NULL")
    Package getRoot(UUID idRestaurant);

    @Query(value = "SELECT p FROM Package p WHERE p.restaurant.id = ?1")
    List<Package> getAllByRestaurant(UUID idRestaurant);
}
