package mr.bpm.bpm_clients.repositories;

import mr.bpm.bpm_clients.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import mr.bpm.bpm_clients.models.EmployeStatus;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    // Méthode utile pour vérifier si un identifiant de connexion existe déjà
    Optional<Employe> findByIdentifiantConnexion(String identifiantConnexion);
    // Cherche les employés dont le nom ou l'identifiant contiennent le terme de recherche,
    // en ignorant la casse (majuscules/minuscules).
    List<Employe> findByNomContainingIgnoreCaseOrIdentifiantConnexionContainingIgnoreCase(String nom, String identifiant);
    long countByStatus(EmployeStatus status);
    List<Employe> findByStatus(EmployeStatus status);

}
