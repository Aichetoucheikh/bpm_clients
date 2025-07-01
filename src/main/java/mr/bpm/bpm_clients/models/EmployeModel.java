package mr.bpm.bpm_clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.models.EmployeStatus;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeModel {

    private Long id;
    private String nom;
    private String identifiantConnexion;
    private Set<Role> roles;
    private EmployeStatus status;
    private String photoUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;
}