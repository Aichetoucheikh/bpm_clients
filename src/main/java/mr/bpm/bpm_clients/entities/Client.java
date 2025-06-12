package mr.bpm.bpm_clients.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cif;      // Numéro d'Identification Fiscale ou identifiant unique client
    private String phone;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    private String currentOtp;
}