package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint pour la connexion.
     * Accepte un corps JSON avec "username" et "password".
     */

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> userInput) {
        // CORRECTION : Utiliser les clés qui correspondent aux paramètres du service
        String identifiant = userInput.get("identifiantConnexion");
        String motDePasse = userInput.get("motDePasse");

        // L'appel au service est maintenant correct
        return authService.login(identifiant, motDePasse);
    }
}