
package mr.bpm.bpm_clients.repositories;

import mr.bpm.bpm_clients.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}