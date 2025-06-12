package mr.bpm.bpm_clients.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "employes") // Nom de la table en base de données
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String identifiantConnexion;

    //@Column(nullable = false)
    private String motDePasse; // Important: ce sera le mot de passe haché

    @Enumerated(EnumType.STRING) // Stocke le nom du rôle ("ADMIN") plutôt qu'un chiffre
    @Column(nullable = false)
    private Role role;
}