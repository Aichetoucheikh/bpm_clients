package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.mappers.EmployeMapper;
import mr.bpm.bpm_clients.models.EmployeModel;
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
        Employe savedEmploye = employeRepository.save(employe);

        return EmployeMapper.map(savedEmploye);
    }

    public Optional<EmployeModel> trouverEmployeParId(Long id) {
        return employeRepository.findById(id).map(EmployeMapper::map);
    }

    public List<EmployeModel> trouverTousLesEmployes() {
        return employeRepository.findAll()
                .stream()
                .map(EmployeMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * NOUVELLE MÉTHODE : Met à jour un employé existant.
     * Si un nouveau mot de passe est fourni, il sera haché et mis à jour.
     * Sinon, l'ancien mot de passe est conservé.
     */
    @Transactional
    public EmployeModel updateEmploye(Long id, EmployeModel employeModel) {
        Employe employeExistant = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id: " + id));

        // Mettre à jour les informations de base
        employeExistant.setNom(employeModel.getNom());
        employeExistant.setRole(employeModel.getRole());

        // Mettre à jour l'identifiant de connexion seulement s'il a changé et est disponible
        if (!employeExistant.getIdentifiantConnexion().equals(employeModel.getIdentifiantConnexion())) {
            employeRepository.findByIdentifiantConnexion(employeModel.getIdentifiantConnexion())
                    .ifPresent(e -> {
                        throw new IllegalStateException("L'identifiant de connexion '" + employeModel.getIdentifiantConnexion() + "' est déjà utilisé.");
                    });
            employeExistant.setIdentifiantConnexion(employeModel.getIdentifiantConnexion());
        }

        // Mettre à jour le mot de passe seulement si un nouveau est fourni
        if (StringUtils.hasText(employeModel.getMotDePasse())) {
            employeExistant.setMotDePasse(passwordEncoder.encode(employeModel.getMotDePasse()));
        }

        Employe updatedEmploye = employeRepository.save(employeExistant);
        return EmployeMapper.map(updatedEmploye);
    }

    /**
     * NOUVELLE MÉTHODE : Supprime un employé.
     * Un admin ne peut pas se supprimer lui-même.
     */
    @Transactional
    public void deleteEmploye(Long id) {
        Employe employeASupprimer = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id: " + id));

        // Vérifier qu'un admin n'essaie pas de se supprimer lui-même
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUsername().equals(employeASupprimer.getIdentifiantConnexion())) {
            throw new IllegalStateException("Un administrateur ne peut pas supprimer son propre compte.");
        }

        employeRepository.deleteById(id);
    }
}