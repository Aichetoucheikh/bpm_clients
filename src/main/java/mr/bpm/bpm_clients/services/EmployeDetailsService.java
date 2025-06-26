package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmployeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeRepository employeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employe employe = employeRepository.findByIdentifiantConnexion(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employé non trouvé avec l'identifiant: " + username));

        // On crée un UserDetails avec le nom, le mot de passe et une liste d'autorisations (rôles)
        // Spring Security ajoutera automatiquement le préfixe "ROLE_" lors de la vérification.
        return new User(employe.getIdentifiantConnexion(), employe.getMotDePasse(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employe.getRole().name())));
    }
}
