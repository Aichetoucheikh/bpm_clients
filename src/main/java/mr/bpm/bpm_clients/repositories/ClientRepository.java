package mr.bpm.bpm_clients.repositories;

import mr.bpm.bpm_clients.entities.Client; // <-- Assurez-vous que cet import pointe vers l'ENTITÉ
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Vous pouvez laisser cet espace vide ou y ajouter des méthodes de recherche personnalisées plus tard si besoin.

}