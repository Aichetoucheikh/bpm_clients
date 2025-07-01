package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.exceptions.DuplicateResourceException;
import mr.bpm.bpm_clients.exceptions.OperationNotAllowedException;
import mr.bpm.bpm_clients.exceptions.ResourceNotFoundException;
import mr.bpm.bpm_clients.mappers.EmployeMapper;
import mr.bpm.bpm_clients.models.EmployeModel;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import mr.bpm.bpm_clients.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set; // <-- Import manquant
import java.util.stream.Collectors;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository; // Dépendance ajoutée ici

    // --- CORRECTION : Injection de toutes les dépendances dans un seul constructeur ---
    @Autowired
    public EmployeService(EmployeRepository employeRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.employeRepository = employeRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository; // Initialisation
    }

    @Transactional
    public EmployeModel creerEmploye(EmployeModel employeModel) {
        employeRepository.findByIdentifiantConnexion(employeModel.getIdentifiantConnexion())
                .ifPresent(e -> {
                    throw new DuplicateResourceException("L'identifiant de connexion '" + employeModel.getIdentifiantConnexion() + "' est déjà utilisé.");
                });

        Employe employe = EmployeMapper.map(employeModel);
        employe.setMotDePasse(passwordEncoder.encode(employeModel.getMotDePasse()));
        employe.setStatus(EmployeStatus.ACTIF);

        Employe savedEmploye = employeRepository.save(employe);
        return EmployeMapper.map(savedEmploye);
    }

    @Transactional(readOnly = true)
    public Optional<EmployeModel> trouverEmployeParId(Long id) {
        return employeRepository.findById(id).map(EmployeMapper::map);
    }

    @Transactional(readOnly = true)
    public List<EmployeModel> rechercherEmployes(String terme) {
        if (!StringUtils.hasText(terme)) {
            return employeRepository.findAll().stream().map(EmployeMapper::map).collect(Collectors.toList());
        }
        List<Employe> employes = employeRepository
                .findByNomContainingIgnoreCaseOrIdentifiantConnexionContainingIgnoreCase(terme, terme);
        return employes.stream().map(EmployeMapper::map).collect(Collectors.toList());
    }

    @Transactional
    public EmployeModel updateEmploye(Long id, EmployeModel employeModel) {
        Employe employeExistant = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'id: " + id));

        employeExistant.setNom(employeModel.getNom());

        if (employeModel.getStatus() != null) {
            employeExistant.setStatus(employeModel.getStatus());
        }

        if (!employeExistant.getIdentifiantConnexion().equals(employeModel.getIdentifiantConnexion())) {
            employeRepository.findByIdentifiantConnexion(employeModel.getIdentifiantConnexion())
                    .ifPresent(e -> {
                        throw new DuplicateResourceException("L'identifiant de connexion '" + employeModel.getIdentifiantConnexion() + "' est déjà utilisé.");
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
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'id: " + id));
        employe.setStatus(EmployeStatus.SUSPENDU);
        return EmployeMapper.map(employeRepository.save(employe));
    }

    @Transactional
    public EmployeModel reactiverEmploye(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'id: " + id));
        employe.setStatus(EmployeStatus.ACTIF);
        return EmployeMapper.map(employeRepository.save(employe));
    }

    @Transactional
    public void deleteEmploye(Long id) {
        Employe employeASupprimer = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'id: " + id));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUsername().equals(employeASupprimer.getIdentifiantConnexion())) {
            throw new OperationNotAllowedException("Un administrateur ne peut pas supprimer son propre compte.");
        }
        employeRepository.deleteById(id);
    }

    // --- Ce bloc est correct, aucune erreur ici ---
    @Transactional
    public EmployeModel assignRolesToEmploye(Long employeId, Set<Long> roleIds) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + employeId));

        Set<Role> roles = roleIds.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé avec l'ID: " + id)))
                .collect(Collectors.toSet());

        employe.setRoles(roles);
        Employe updatedEmploye = employeRepository.save(employe);
        return EmployeMapper.map(updatedEmploye);
    }
}