package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import mr.bpm.bpm_clients.configs.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

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
     * Gère la logique de connexion avec des messages d'erreur spécifiques.
     * @param identifiantConnexion L'identifiant de l'employé.
     * @param motDePasse Le mot de passe en clair.
     * @return Une Map contenant le token en cas de succès.
     * @throws ResponseStatusException avec un statut 401 ou 403 en cas d'échec.
     */
    public Map<String, String> login(String identifiantConnexion, String motDePasse) {

        // 1. Chercher l'employé par son identifiant
        Optional<Employe> employeOptional = employeRepository.findByIdentifiantConnexion(identifiantConnexion);

        if (employeOptional.isEmpty()) {
            // Si l'utilisateur n'existe pas, renvoyer une erreur générique pour ne pas donner d'indices (sécurité)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiant ou mot de passe incorrect.");
        }

        Employe employe = employeOptional.get();

        // 2. Vérifier si le mot de passe est correct
        if (!passwordEncoder.matches(motDePasse, employe.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiant ou mot de passe incorrect.");
        }

        // 3. Vérifier si le compte est suspendu (SEULEMENT si le mot de passe est correct)
        if (employe.getStatus() == EmployeStatus.SUSPENDU) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Votre compte a été suspendu.");
        }

        // 4. Si tout est correct, générer et retourner le token
        String token = jwtUtil.generateToken(employe.getIdentifiantConnexion());
        return Map.of(
                "token", token,
                "role", employe.getRole().name(),
                "nom", employe.getNom(),
                "photoUrl", employe.getPhotoUrl() != null ? employe.getPhotoUrl() : ""
        );

    }
}