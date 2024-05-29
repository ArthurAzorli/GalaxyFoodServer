package br.edu.ifsp.galaxyfood.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.galaxyfood.server.model.domain.Package;

import java.util.UUID;

public interface PackageDAO extends JpaRepository<Package, UUID> {
    Package getPackageById(UUID id);
}
