package mr.bpm.bpm_clients.mappers;

import mr.bpm.bpm_clients.entities.Client;
import mr.bpm.bpm_clients.models.ClientModel;
// It's good practice to use a logger, but not strictly necessary for this fix
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class ClientMapper {

    // private static final Logger logger = LoggerFactory.getLogger(ClientMapper.class); // Optional

    public static ClientModel map(Client client) {
        if (client == null) {
            return null;
        }

        // The error indicates client.getStatus() is already of type ClientStatus (enum).
        // Therefore, no string parsing or ClientStatus.valueOf() is needed here.
        // The .trim() call was incorrect because client.getStatus() is not a String.
        return ClientModel.builder()
                .id(client.getId())
                .name(client.getName())
                .cif(client.getCif())
                .phone(client.getPhone())
                .status(client.getStatus()) // Direct assignment
                .currentOtp(client.getCurrentOtp()) // Assuming Client entity has currentOtp
                .build();
    }

    public static Client map(ClientModel clientModel) {
        if (clientModel == null) {
            return null;
        }

        // Assuming your Client entity also uses the ClientStatus enum for its status field.
        // If Client entity's status field MUST be a String, then you would use:
        // String statusString = (clientModel.getStatus() == null) ? null : clientModel.getStatus().name();
        // and then .status(statusString) in the builder.
        // However, the error in the other map method implies the entity uses the enum.
        return Client.builder()
                .id(clientModel.getId())
                .name(clientModel.getName())
                .cif(clientModel.getCif())
                .phone(clientModel.getPhone())
                .status(clientModel.getStatus()) // Direct assignment
                .currentOtp(clientModel.getCurrentOtp()) // Assuming ClientModel has currentOtp
                .build();
    }
}