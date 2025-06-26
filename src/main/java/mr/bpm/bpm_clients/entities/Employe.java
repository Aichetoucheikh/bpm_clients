package mr.bpm.bpm_clients.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.bpm.bpm_clients.models.EmployeStatus; // NOUVEL IMPORT
import mr.bpm.bpm_clients.entities.Role;

@Entity
@Table(name = "employes")
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

    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // --- NOUVEAU CHAMP ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeStatus status;
    private String photoUrl;
}