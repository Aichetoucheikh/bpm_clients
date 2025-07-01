package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.models.LoginResponse;
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

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Map<String, String> userInput) {
        String identifiant = userInput.get("identifiantConnexion");
        String motDePasse = userInput.get("motDePasse");

        return authService.login(identifiant, motDePasse);
    }
}