package mr.bpm.bpm_clients.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // CHANGEMENT 1 : Nouvel import
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // CHANGEMENT 2 : Nouvelle annotation pour la sécurité des méthodes
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF (non nécessaire pour une API stateless)
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // Enable CORS
                // Configurer les autorisations de requêtes avec la nouvelle syntaxe
                .authorizeHttpRequests(auth -> auth
                        // CHANGEMENT 3 : Remplacement de antMatchers par requestMatchers
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/clients/block/**", "/clients/otp/**").hasAnyRole("ADMIN", "SUPERVISEUR")
                        .requestMatchers("/clients/unblock/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // Configurer la gestion de session en stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Ajouter notre filtre JWT avant le filtre de base de Spring
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean nécessaire pour l'injection de l'AuthenticationManager dans d'anciennes configurations
    // Avec la nouvelle configuration, il est souvent fourni par la config par défaut
    // mais il est bon de l'exposer explicitement si nécessaire.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
