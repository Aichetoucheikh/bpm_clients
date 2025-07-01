package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.models.ClientModel;
import mr.bpm.bpm_clients.models.EmployeModel;
import mr.bpm.bpm_clients.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Page<ClientModel> clients = clientService.getAllClients(page, size);

        // Reformat la réponse pour Angular
        return ResponseEntity.ok(
                Map.of(
                        "content", clients.getContent(),
                        "totalPages", clients.getTotalPages(),
                        "number", clients.getNumber(),
                        "size", clients.getSize(),
                        "totalElements", clients.getTotalElements()
                )
        );
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchByPhone(@RequestParam String phone,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size) {
        Page<ClientModel> result = clientService.searchClientsByPhone(phone, page, size);
        return ResponseEntity.ok(
                Map.of(
                        "content", result.getContent(),
                        "totalPages", result.getTotalPages(),
                        "number", result.getNumber()
                )
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ClientModel client = clientService.getClientById(id).orElseThrow(RuntimeException::new);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClientModel clientModel) {
        try {
            ClientModel nouveauClient = clientService.creerClient(clientModel);
            return new ResponseEntity<>(nouveauClient, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody ClientModel clientModel) {
        try {
            ClientModel updatedClient = clientService.updateClient(id, clientModel);
            return ResponseEntity.ok(updatedClient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PostMapping("/{id}/block")
    @PreAuthorize("hasAuthority('BLOCK_CLIENT')") // Sécurisé par permission
    public ResponseEntity<?> blockClient(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String motif = payload.get("motif");
        if (motif == null || motif.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le motif de blocage est obligatoire."));
        }
        ClientModel clientBloque = clientService.bloquerClient(id, motif);
        return ResponseEntity.ok(clientBloque);
    }


    @PostMapping("/{id}/unblock")
    @PreAuthorize("hasAuthority('UNBLOCK_CLIENT')") // Sécurisé par la permission UNBLOCK_CLIENT
    public ResponseEntity<?> unblockClient(@PathVariable Long id) {
        // La simulation d'employé a été supprimée. Spring Security s'en occupe.
        ClientModel clientDebloque = clientService.debloquerClient(id);
        return ResponseEntity.ok(clientDebloque);
    }
}