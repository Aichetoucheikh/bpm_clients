package mr.bpm.bpm_clients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.bpm.bpm_clients.entities.ClientStatus;


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


    private ClientStatus status;
    private String motifBlocage;
    private String currentOtp; // OTP actuel du client
    private String nni;
    private String sexe;
    private String photoUrl;
}