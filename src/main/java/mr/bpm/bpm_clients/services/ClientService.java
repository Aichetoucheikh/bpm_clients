package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Client;
import mr.bpm.bpm_clients.mappers.ClientMapper;
import mr.bpm.bpm_clients.models.ClientModel;
import mr.bpm.bpm_clients.entities.ClientStatus;
import mr.bpm.bpm_clients.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Récupère une liste paginée de clients.
     * Le type de retour est bien Page<ClientModel>.
     */
    public Page<ClientModel> getAllClients(int page, int size) {
        Page<Client> clientsPage = clientRepository.findAll(PageRequest.of(page, size));
        return clientsPage.map(ClientMapper::map); // Correct : on retourne bien une page de modèles
    }

    /**
     * Récupère un client par son ID.
     * Le type de retour est bien Optional<ClientModel>.
     */
    public Optional<ClientModel> getClientById(Long id) {
        // On récupère une entité, puis on la mappe en modèle pour le retour
        return clientRepository.findById(id).map(ClientMapper::map);
    }

    @Transactional
    public ClientModel creerClient(ClientModel clientModel) {
        clientModel.setStatus(ClientStatus.ACTIVE);
        clientModel.setCurrentOtp(UUID.randomUUID().toString().substring(0, 6).toUpperCase());

        // IMPORTANT : Mapper le modèle en entité AVANT de sauvegarder
        Client clientEntity = ClientMapper.map(clientModel);
        Client savedClientEntity = clientRepository.save(clientEntity);

        // Mapper l'entité sauvegardée en modèle pour le retour
        return ClientMapper.map(savedClientEntity);
    }

    @Transactional
    public ClientModel updateClient(Long id, ClientModel clientModel) {

        // On récupère l'entité depuis la base de données
        Client clientExistant = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));

        clientExistant.setName(clientModel.getName());
        clientExistant.setCif(clientModel.getCif());
        clientExistant.setPhone(clientModel.getPhone());

        // IMPORTANT : On sauvegarde l'entité mise à jour
        Client updatedClient = clientRepository.save(clientExistant);
        return ClientMapper.map(updatedClient);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Tentative de suppression d'un client non trouvé avec l'id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Transactional
    public ClientModel blockClient(Long clientId) {
        // On récupère l'entité
        Client clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        if (clientEntity.getStatus() == ClientStatus.BLOCKED) {
            throw new IllegalStateException("Le client " + clientEntity.getName() + " est déjà bloqué.");
        }
        clientEntity.setStatus(ClientStatus.BLOCKED);

        // On sauvegarde l'entité
        Client updatedClient = clientRepository.save(clientEntity);
        return ClientMapper.map(updatedClient);
    }

    @Transactional
    public ClientModel unblockClient(Long clientId) {
        // On récupère l'entité
        Client clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        if (clientEntity.getStatus() == ClientStatus.ACTIVE) {
            throw new IllegalStateException("Le client " + clientEntity.getName() + " est déjà actif.");
        }
        clientEntity.setStatus(ClientStatus.ACTIVE);

        // On sauvegarde l'entité
        Client updatedClient = clientRepository.save(clientEntity);
        return ClientMapper.map(updatedClient);
    }

    public String getClientOtp(Long clientId) {

        Client clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        return "L'OTP pour le client '" + clientEntity.getName() + "' est : " + clientEntity.getCurrentOtp();
    }
}