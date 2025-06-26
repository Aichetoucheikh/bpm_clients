package mr.bpm.bpm_clients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.bpm.bpm_clients.entities.ClientStatus;

// Décommentez les annotations JPA si vous l'utilisez comme entité
// import javax.persistence.Entity;
// import javax.persistence.EnumType;
// import javax.persistence.Enumerated;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;

// @Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientModel {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cif;      // Numéro d'Identification Fiscale ou identifiant unique client
    private String phone;

    // @Enumerated(EnumType.STRING) // Recommandé pour stocker le nom du statut en BDD
    private ClientStatus status;
    private String motifBlocage;
    private String currentOtp; // OTP actuel du client
    private String nni;
    private String sexe;
    private String photoUrl;
}