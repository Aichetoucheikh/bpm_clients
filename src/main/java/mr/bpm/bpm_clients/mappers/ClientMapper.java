package mr.bpm.bpm_clients.mappers;

import mr.bpm.bpm_clients.entities.Client;
import mr.bpm.bpm_clients.models.ClientModel;

public class ClientMapper {

    /**
     * Convertit une entité Client en modèle ClientModel.
     */
    public static ClientModel map(Client client) {
        if (client == null) {
            return null;
        }
        return ClientModel.builder()
                .id(client.getId())
                .name(client.getName())
                .cif(client.getCif())
                .phone(client.getPhone())
                .status(client.getStatus())
                .currentOtp(client.getCurrentOtp())
                .motifBlocage(client.getMotifBlocage())
                .nni(client.getNni())
                .sexe(client.getSexe())
                .photoUrl(client.getPhotoUrl())
                .build();
    }

    /**
     * Convertit un modèle ClientModel en entité Client.
     */
    public static Client map(ClientModel clientModel) {
        if (clientModel == null) {
            return null;
        }
        return Client.builder()
                .id(clientModel.getId())
                .name(clientModel.getName())
                .cif(clientModel.getCif())
                .phone(clientModel.getPhone())
                .status(clientModel.getStatus())
                .currentOtp(clientModel.getCurrentOtp())
                .motifBlocage(clientModel.getMotifBlocage())
                .nni(clientModel.getNni())
                .sexe(clientModel.getSexe())
                .photoUrl(clientModel.getPhotoUrl())
                .build();
    }
}
