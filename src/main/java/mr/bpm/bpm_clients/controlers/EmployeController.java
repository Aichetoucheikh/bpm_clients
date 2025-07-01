package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.models.EmployeModel;
import mr.bpm.bpm_clients.services.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employes") // L'URL de base pour toutes les routes
@PreAuthorize("hasAuthority('MANAGE_EMPLOYES')") // Protège TOUTES les routes de ce contrôleur
public class EmployeController {

    private final EmployeService employeService;

    @Autowired
    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @PostMapping
    public ResponseEntity<?> creerEmploye(@RequestBody EmployeModel employeModel) {
        try {
            EmployeModel nouvelEmploye = employeService.creerEmploye(employeModel);
            return new ResponseEntity<>(nouvelEmploye, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeModel>> listerOuRechercherEmployes(@RequestParam(required = false) String recherche) {
        List<EmployeModel> employes = employeService.rechercherEmployes(recherche);
        return ResponseEntity.ok(employes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeModel> trouverEmployeParId(@PathVariable Long id) {
        return employeService.trouverEmployeParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Les autres endpoints restent les mêmes... (suspendre, reactiver, etc.)
    @PostMapping("/{id}/suspend")
    public ResponseEntity<EmployeModel> suspendreEmploye(@PathVariable Long id) {
        EmployeModel employe = employeService.suspendreEmploye(id);
        return ResponseEntity.ok(employe);
    }

    @PostMapping("/{id}/reactivate")
    public ResponseEntity<EmployeModel> reactiverEmploye(@PathVariable Long id) {
        EmployeModel employe = employeService.reactiverEmploye(id);
        return ResponseEntity.ok(employe);
    }
    @PutMapping("/{id}/roles")
    public ResponseEntity<EmployeModel> assignRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        EmployeModel updatedEmploye = employeService.assignRolesToEmploye(id, roleIds);
        return ResponseEntity.ok(updatedEmploye);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerEmploye(@PathVariable Long id) {
        try {
            employeService.deleteEmploye(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
