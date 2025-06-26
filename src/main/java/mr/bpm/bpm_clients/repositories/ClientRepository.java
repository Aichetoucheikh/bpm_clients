package mr.bpm.bpm_clients.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mr.bpm.bpm_clients.entities.Client; // <-- Assurez-vous que cet import pointe vers l'ENTITÉ
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mr.bpm.bpm_clients.entities.ClientStatus;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Vous pouvez laisser cet espace vide ou y ajouter des méthodes de recherche personnalisées plus tard si besoin.

    Page<Client> findByPhoneContaining(String phone, Pageable pageable);
    long countByStatus(ClientStatus status);
}