package br.edu.ifsp.galaxyfood.server.model.repository;

import br.edu.ifsp.galaxyfood.server.model.domain.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageItemDAO extends JpaRepository<PackageItem, UUID> {

    PackageItem getPackageItemById(UUID id);
}
