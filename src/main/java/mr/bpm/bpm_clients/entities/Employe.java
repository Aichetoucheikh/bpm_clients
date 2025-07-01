// src/main/java/mr/bpm/bpm_clients/entities/Employe.java
package mr.bpm.bpm_clients.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;   // <-- Ajouter cet import
import lombok.Setter;
import lombok.NoArgsConstructor;
import mr.bpm.bpm_clients.models.EmployeStatus;
import java.util.Set; // <-- Nouvel import

@Entity
@Table(name = "employes")
@Getter
@Setter
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

    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private Role role;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employe_roles",
            joinColumns = @JoinColumn(name = "employe_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeStatus status;

    private String photoUrl;
}