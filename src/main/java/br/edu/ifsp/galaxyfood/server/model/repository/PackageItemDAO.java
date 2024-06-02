package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PackageItemDAO extends JpaRepository<PackageItem, UUID> {

    @Query(value = "SELECT p FROM PackageItem p WHERE p.id = ?1")
    PackageItem getPackageItemById(UUID id);
}
