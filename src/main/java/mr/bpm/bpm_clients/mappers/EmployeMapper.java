package mr.bpm.bpm_clients.mappers;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.models.EmployeModel;

public class EmployeMapper {

    /**
     * Convertit une entité Employe en modèle EmployeModel.
     * Le mot de passe n'est JAMAIS inclus.
     */
    public static EmployeModel map(Employe employe) {
        if (employe == null) {
            return null;
        }
        return EmployeModel.builder()
                .id(employe.getId())
                .nom(employe.getNom())
                .identifiantConnexion(employe.getIdentifiantConnexion())
                .role(employe.getRole())
                .status(employe.getStatus())
                // On ne mappe JAMAIS le mot de passe vers le modèle
                .photoUrl(employe.getPhotoUrl())
                .build();
    }

    /**
     * Convertit un modèle EmployeModel en entité Employe.
     * Le mot de passe est inclus pour la création/sauvegarde.
     */
    public static Employe map(EmployeModel employeModel) {
        if (employeModel == null) {
            return null;
        }
        return Employe.builder()
                .id(employeModel.getId())
                .nom(employeModel.getNom())
                .identifiantConnexion(employeModel.getIdentifiantConnexion())
                .motDePasse(employeModel.getMotDePasse()) // Le mot de passe est mappé ici
                .role(employeModel.getRole())
                .status(employeModel.getStatus())
                .photoUrl(employeModel.getPhotoUrl())
                .build();
    }
}