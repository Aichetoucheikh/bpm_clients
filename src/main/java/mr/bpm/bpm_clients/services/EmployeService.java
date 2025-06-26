package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.mappers.EmployeMapper;
import mr.bpm.bpm_clients.models.EmployeModel;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeService(EmployeRepository employeRepository, PasswordEncoder passwordEncoder) {
        this.employeRepository = employeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmployeModel creerEmploye(EmployeModel employeModel) {
        employeRepository.findByIdentifiantConnexion(employeModel.getIdentifiantConnexion())
                .ifPresent(e -> {
                    throw new IllegalStateException("L'identifiant de connexion '" + employeModel.getIdentifiantConnexion() + "' est déjà utilisé.");
                });

        Employe employe = EmployeMapper.map(employeModel);
        employe.setMotDePasse(passwordEncoder.encode(employeModel.getMotDePasse()));

        // --- CORRECTION CRITIQUE : Initialiser le statut ---
        employe.setStatus(EmployeStatus.ACTIF);

        Employe savedEmploye = employeRepository.save(employe);
        return EmployeMapper.map(savedEmploye);
    }

    // ... (Le reste de votre service : trouverEmployeParId, suspendreEmploye, etc.)
    // Assurez-vous que toutes vos autres méthodes sont ici.

    public Optional<EmployeModel> trouverEmployeParId(Long id) {
        return employeRepository.findById(id).map(EmployeMapper::map);
    }

    public List<EmployeModel> trouverTousLesEmployes() {
        return employeRepository.findAll()
                .stream()
                .map(EmployeMapper::map)
                .collect(Collectors.toList());
    }

    public List<EmployeModel> rechercherEmployes(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return trouverTousLesEmployes();
        }
        List<Employe> employes = employeRepository
                .findByNomContainingIgnoreCaseOrIdentifiantConnexionContainingIgnoreCase(terme, terme);

        return employes.stream()
                .map(EmployeMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeModel updateEmploye(Long id, EmployeModel employeModel) {
        Employe employeExistant = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id: " + id));

        employeExistant.setNom(employeModel.getNom());
        employeExistant.setRole(employeModel.getRole());
        employeExistant.setStatus(employeModel.getStatus());

        if (!employeExistant.getIdentifiantConnexion().equals(employeModel.getIdentifiantConnexion())) {
            employeRepository.findByIdentifiantConnexion(employeModel.getIdentifiantConnexion())
                    .ifPresent(e -> {
                        throw new IllegalStateException("L'identifiant de connexion '" + employeModel.getIdentifiantConnexion() + "' est déjà utilisé.");
                    });
            employeExistant.setIdentifiantConnexion(employeModel.getIdentifiantConnexion());
        }

        if (StringUtils.hasText(employeModel.getMotDePasse())) {
            employeExistant.setMotDePasse(passwordEncoder.encode(employeModel.getMotDePasse()));
        }

        Employe updatedEmploye = employeRepository.save(employeExistant);
        return EmployeMapper.map(updatedEmploye);
    }

    @Transactional
    public EmployeModel suspendreEmploye(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        employe.setStatus(EmployeStatus.SUSPENDU);
        return EmployeMapper.map(employeRepository.save(employe));
    }

    @Transactional
    public EmployeModel reactiverEmploye(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        employe.setStatus(EmployeStatus.ACTIF);
        return EmployeMapper.map(employeRepository.save(employe));
    }

    @Transactional
    public void deleteEmploye(Long id) {
        Employe employeASupprimer = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id: " + id));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUsername().equals(employeASupprimer.getIdentifiantConnexion())) {
            throw new IllegalStateException("Un administrateur ne peut pas supprimer son propre compte.");
        }

        employeRepository.deleteById(id);
    }
}