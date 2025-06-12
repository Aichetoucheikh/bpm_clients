package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import mr.bpm.bpm_clients.configs.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final EmployeRepository employeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(EmployeRepository employeRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.employeRepository = employeRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gère la logique de connexion.
     * @param identifiantConnexion identifiant de connexion de l'employé.
     * @param motDePasse mot de passe en clair de l'employé.
     * @return une Map contenant le token si la connexion réussit, ou une erreur sinon.
     */
    public Map<String, String> login(String identifiantConnexion, String motDePasse) {
        // On cherche l'employé par son identifiant de connexion
        Employe employe = employeRepository.findByIdentifiantConnexion(identifiantConnexion).orElse(null);

        // Vérification du mot de passe
        if (employe != null && passwordEncoder.matches(motDePasse, employe.getMotDePasse())) {

            // Génération du token JWT
            String token = jwtUtil.generateToken(identifiantConnexion);

            return Map.of("token", token);
        }

        // Retour en cas d'échec
        return Map.of("error", "Identifiant ou mot de passe incorrect");
    }
}