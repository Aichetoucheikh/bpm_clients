// src/main/java/mr/bpm/bpm_clients/BpmClientsApplication.java
package mr.bpm.bpm_clients;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.entities.Permission;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import mr.bpm.bpm_clients.repositories.PermissionRepository;
import mr.bpm.bpm_clients.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BpmClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmClientsApplication.class, args);
	}

	@Bean
	@Transactional
		// Important pour les opérations sur les entités liées
	CommandLineRunner commandLineRunner(EmployeRepository employeRepository,
										RoleRepository roleRepository,
										PermissionRepository permissionRepository,
										PasswordEncoder passwordEncoder) {
		return args -> {
			// Ne s'exécute que si la base de données est vide pour éviter les doublons
			if (permissionRepository.count() == 0) {
				System.out.println("Création des permissions par défaut...");
				Permission manageEmployes = new Permission("MANAGE_EMPLOYES", "Gérer les employés");
				Permission manageRoles = new Permission("MANAGE_ROLES_PERMISSIONS", "Gérer les rôles et permissions");
				Permission blockClient = new Permission("BLOCK_CLIENT", "Bloquer un client");
				Permission unblockClient = new Permission("UNBLOCK_CLIENT", "Débloquer un client");
				permissionRepository.saveAll(List.of(manageEmployes, manageRoles, blockClient, unblockClient));

				System.out.println("Création des rôles et de l'admin par défaut...");

				// Rôle ADMIN avec toutes les permissions
				Role adminRole = new Role("ADMINISTRATEUR");
				adminRole.setPermissions(Set.copyOf(permissionRepository.findAll()));
				roleRepository.save(adminRole);

				// Rôle SUPERVISEUR
				Role supervisorRole = new Role("SUPERVISEUR");
				supervisorRole.setPermissions(Set.of(blockClient));
				roleRepository.save(supervisorRole);

				// Création de l'employé admin
				Employe admin = Employe.builder()
						.nom("Admin Principal")
						.identifiantConnexion("admin")
						.motDePasse(passwordEncoder.encode("admin123"))
						.roles(Set.of(adminRole))
						.status(EmployeStatus.ACTIF)
						.build();
				employeRepository.save(admin);
				System.out.println("Utilisateur admin créé : admin / admin123");
			}
		};
	}
}