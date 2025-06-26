package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.ClientStatus;
import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.mappers.EmployeMapper;
import mr.bpm.bpm_clients.models.DashboardDataDto;
import mr.bpm.bpm_clients.models.EmployeModel;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.repositories.ClientRepository;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ClientRepository clientRepository;
    private final EmployeRepository employeRepository;

    @Autowired
    public DashboardService(ClientRepository clientRepository, EmployeRepository employeRepository) {
        this.clientRepository = clientRepository;
        this.employeRepository = employeRepository;
    }

    public DashboardDataDto getDashboardData() {
        // --- Statistiques sur les clients ---
        long totalClients = clientRepository.count();
        long clientsActifs = clientRepository.countByStatus(ClientStatus.ACTIVE);
        long clientsBloques = totalClients - clientsActifs;

        // --- Statistiques sur les employés ---
        long totalEmployes = employeRepository.count();

        // --- Liste des employés suspendus ---
        List<Employe> employesSuspendusEntites = employeRepository.findByStatus(EmployeStatus.SUSPENDU);
        List<EmployeModel> employesSuspendusModel = employesSuspendusEntites.stream()
                .map(EmployeMapper::map)
                .collect(Collectors.toList());

        // --- Construction de l'objet de réponse ---
        return DashboardDataDto.builder()
                .totalClients(totalClients)
                .clientsActifs(clientsActifs)
                .clientsBloques(clientsBloques)
                .totalEmployes(totalEmployes)
                .employesSuspendus(employesSuspendusModel)
                .build();
    }
}
