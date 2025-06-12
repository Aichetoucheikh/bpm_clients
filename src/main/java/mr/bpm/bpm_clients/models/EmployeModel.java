package mr.bpm.bpm_clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.bpm.bpm_clients.entities.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeModel {

    private Long id;
    private String nom;
    private String identifiantConnexion;
    private Role role;

    // Ce champ ne sera utilisé que pour la création/mise à jour du mot de passe.
    // Il ne sera jamais renvoyé au client.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;
}