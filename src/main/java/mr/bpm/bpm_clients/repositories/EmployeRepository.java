package mr.bpm.bpm_clients.repositories;

import mr.bpm.bpm_clients.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import mr.bpm.bpm_clients.models.EmployeStatus;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Optional<Employe> findByIdentifiantConnexion(String identifiantConnexion);

    List<Employe> findByNomContainingIgnoreCaseOrIdentifiantConnexionContainingIgnoreCase(String nom, String identifiant);

    long countByStatus(EmployeStatus status);

    List<Employe> findByStatus(EmployeStatus status);

    /**
     * Trouve un employé par son identifiant et charge immédiatement les rôles et les permissions associées.
     * Utile pour récupérer l'utilisateur avec toutes ses autorités dans un contexte de sécurité.
     */
    @Query("""
            SELECT e FROM Employe e
            LEFT JOIN FETCH e.roles r
            LEFT JOIN FETCH r.permissions
            WHERE e.identifiantConnexion = :identifiant
           """)
    Optional<Employe> findWithRolesAndPermissionsByIdentifiant(@Param("identifiant") String identifiant);
}