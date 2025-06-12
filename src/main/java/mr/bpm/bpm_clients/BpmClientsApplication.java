package mr.bpm.bpm_clients;

// Imports nécessaires
import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			// On vérifie si la table des employés est vide avant d'insérer
			if (employeRepository.count() == 0) {
				System.out.println("Base de données vide. Création des utilisateurs par défaut...");

				// Création de l'ADMIN
				Employe admin = Employe.builder()
						.nom("Admin Principal")
						.identifiantConnexion("admin")
						.motDePasse(passwordEncoder.encode("admin123")) // Mot de passe en clair haché
						.role(Role.ADMIN)
						.build();

				// Création du SUPERVISEUR
				Employe supervisor = Employe.builder()
						.nom("Superviseur Test")
						.identifiantConnexion("superviseur")
						.motDePasse(passwordEncoder.encode("sup123"))
						.role(Role.SUPERVISEUR)
						.build();

				// Création de l'AGENT
				Employe agent = Employe.builder()
						.nom("Agent Bankily")
						.identifiantConnexion("agent")
						.motDePasse(passwordEncoder.encode("agent123"))
						.role(Role.AGENT_BANKILY)
						.build();

				// Sauvegarde de tous les utilisateurs
				employeRepository.save(admin);
				employeRepository.save(supervisor);
				employeRepository.save(agent);

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