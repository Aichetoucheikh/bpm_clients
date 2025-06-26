package mr.bpm.bpm_clients;

// Imports nécessaires
import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BpmClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmClientsApplication.class, args);
	}

	/**
	 * NOUVEAU CODE : Ce bean s'exécutera au démarrage de l'application.
	 * Il va créer des utilisateurs par défaut si la base de données est vide.
	 *
	 * @param employeRepository Le repository pour sauvegarder les employés.
	 * @param passwordEncoder Le service pour hacher les mots de passe.
	 * @return Une instance de CommandLineRunner.
	 */
	@Bean
	CommandLineRunner commandLineRunner(EmployeRepository employeRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (employeRepository.count() == 0) {
				// ...
				Employe admin = Employe.builder()
						.nom("Admin Principal")
						.identifiantConnexion("admin")
						.motDePasse(passwordEncoder.encode("admin123"))
						.role(Role.ADMIN)
						.status(EmployeStatus.ACTIF)
						.photoUrl("https://i.pravatar.cc/150?u=admin") // AJOUTER
						.build();

				Employe supervisor = Employe.builder()
						.nom("Superviseur Test")
						.identifiantConnexion("superviseur")
						.motDePasse(passwordEncoder.encode("sup123"))
						.role(Role.SUPERVISEUR)
						.status(EmployeStatus.ACTIF)
						.photoUrl("https://i.pravatar.cc/150?u=supervisor") // AJOUTER
						.build();

				Employe agent = Employe.builder()
						.nom("Agent Bankily")
						.identifiantConnexion("agent")
						.motDePasse(passwordEncoder.encode("agent123"))
						.role(Role.AGENT_BANKILY)
						.status(EmployeStatus.ACTIF)
						.photoUrl("https://i.pravatar.cc/150?u=agent") // AJOUTER
						.build();

				employeRepository.saveAll(List.of(admin, supervisor, agent));

				System.out.println("Utilisateurs par défaut créés avec succès !");
				System.out.println("Admin -> identifiant: admin, mdp: admin123");
				System.out.println("Superviseur -> identifiant: superviseur, mdp: sup123");
				System.out.println("Agent -> identifiant: agent, mdp: agent123");
			} else {
				System.out.println("La base de données contient déjà des utilisateurs. Aucune action requise.");
			}
		};
	}
}