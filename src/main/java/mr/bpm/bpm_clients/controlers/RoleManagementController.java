// src/main/java/mr/bpm/bpm_clients/controlers/RoleManagementController.java
package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.entities.Permission;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.services.RoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/management")

@PreAuthorize("hasAuthority('MANAGE_ROLES_PERMISSIONS')")
public class RoleManagementController {

    @Autowired
    private RoleManagementService roleManagementService;

    // --- Endpoints pour les Rôles ---
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleManagementService.findAllRoles();
    }

    @PostMapping("/roles")
    public Role createRole(@RequestBody Map<String, String> payload) {
        String roleName = payload.get("name");
        return roleManagementService.createRole(roleName);
    }

    @PostMapping("/roles/{roleId}/permissions")
    public Role assignPermissions(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds) {
        return roleManagementService.assignPermissionsToRole(roleId, permissionIds);
    }

    // --- Endpoint pour les Permissions ---
    @GetMapping("/permissions")
    public List<Permission> getAllPermissions() {
        return roleManagementService.findAllPermissions();
    }
}