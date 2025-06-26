package mr.bpm.bpm_clients.entities;
import jakarta.persistence.Lob; // Import pour les textes longs
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cif;      // Numéro d'Identification Fiscale ou identifiant unique client
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status;

    private String currentOtp;

    @Lob // Indique que ce champ peut contenir un texte long en BDD
    private String motifBlocage;
    private String nni;
    private String sexe;
    private String photoUrl;
}

