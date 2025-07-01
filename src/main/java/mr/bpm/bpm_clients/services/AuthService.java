package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Employe;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.models.EmployeStatus;
import mr.bpm.bpm_clients.models.LoginResponse;
import mr.bpm.bpm_clients.repositories.EmployeRepository;
import mr.bpm.bpm_clients.configs.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public LoginResponse login(String identifiantConnexion, String motDePasse) {

        Optional<Employe> employeOptional = employeRepository.findByIdentifiantConnexion(identifiantConnexion);

        if (employeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiant ou mot de passe incorrect.");
        }

        Employe employe = employeOptional.get();

        if (!passwordEncoder.matches(motDePasse, employe.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiant ou mot de passe incorrect.");
        }

        if (employe.getStatus() == EmployeStatus.SUSPENDU) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Votre compte a été suspendu.");
        }

        String token = jwtUtil.generateToken(employe.getIdentifiantConnexion());

        String roles = employe.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(","));

        List<String> permissions = employe.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName())
                .distinct()
                .collect(Collectors.toList());

        return new LoginResponse(
                token,
                roles,
                employe.getNom(),
                employe.getPhotoUrl() != null ? employe.getPhotoUrl() : "",
                permissions
        );
    }
}