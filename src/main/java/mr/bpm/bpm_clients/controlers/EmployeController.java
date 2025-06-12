package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.models.EmployeModel;
import mr.bpm.bpm_clients.services.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employes") // Un endpoint dédié pour la gestion des employés
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeModel>> listerEmployes() {
        List<EmployeModel> employes = employeService.trouverTousLesEmployes();
        return ResponseEntity.ok(employes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeModel> trouverEmployeParId(@PathVariable Long id) {
        return employeService.trouverEmployeParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> mettreAJourEmploye(@PathVariable Long id, @RequestBody EmployeModel employeModel) {
        try {
            EmployeModel employeMisAJour = employeService.updateEmploye(id, employeModel);
            return ResponseEntity.ok(employeMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerEmploye(@PathVariable Long id) {
        try {
            employeService.deleteEmploye(id);
            return ResponseEntity.noContent().build(); // Statut 204 No Content
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}