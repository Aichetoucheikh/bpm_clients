package mr.bpm.bpm_clients.repositories;

import mr.bpm.bpm_clients.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    // Méthode utile pour vérifier si un identifiant de connexion existe déjà
    Optional<Employe> findByIdentifiantConnexion(String identifiantConnexion);
}