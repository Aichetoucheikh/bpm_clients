package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.entities.Role; // Assurez-vous que cet import est présent
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeRepository employeRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employe employe = employeRepository.findWithRolesAndPermissionsByIdentifiant(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employé non trouvé: " + username));

        Stream<GrantedAuthority> permissionAuthorities = employe.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()));

        Stream<GrantedAuthority> roleAuthorities = employe.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()));

        Collection<? extends GrantedAuthority> authorities = Stream.concat(permissionAuthorities, roleAuthorities)
                .collect(Collectors.toSet());

        // --- BLOC DE DÉBOGAGE ---
        System.out.println("\n----------- DÉBOGAGE DES AUTORISATIONS -----------");
        System.out.println("Utilisateur authentifié : " + employe.getIdentifiantConnexion());
        System.out.println("Rôles trouvés en base de données : " +
                employe.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", ")));
        System.out.println("Autorisations finales chargées dans Spring Security :");
        if (authorities.isEmpty()) {
            System.out.println("-> AUCUNE AUTORISATION CHARGÉE !");
        } else {
            authorities.forEach(auth -> System.out.println("-> " + auth.getAuthority()));
        }
        System.out.println("-------------------------------------------------\n");
        // --- FIN DU BLOC DE DÉBOGAGE ---

        return new User(employe.getIdentifiantConnexion(), employe.getMotDePasse(), authorities);
    }
}