
package mr.bpm.bpm_clients.services;

import mr.bpm.bpm_clients.entities.Permission;
import mr.bpm.bpm_clients.entities.Role;
import mr.bpm.bpm_clients.exceptions.DuplicateResourceException;
import mr.bpm.bpm_clients.exceptions.ResourceNotFoundException;
import mr.bpm.bpm_clients.repositories.PermissionRepository;
import mr.bpm.bpm_clients.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleManagementService {
    @Autowired private RoleRepository roleRepository;
    @Autowired private PermissionRepository permissionRepository;

    @Transactional
    public Role createRole(String name) {
        roleRepository.findByName(name).ifPresent(r -> {
            throw new DuplicateResourceException("Le rôle '" + name + "' existe déjà.");
        });
        Role newRole = new Role(name);
        return roleRepository.save(newRole);
    }

    public List<Role> findAllRoles() { return roleRepository.findAll(); }
    public List<Permission> findAllPermissions() { return permissionRepository.findAll(); }

    @Transactional
    public Role assignPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé avec l'ID: " + roleId));

        Set<Permission> permissions = permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission non trouvée avec l'ID: " + id)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }
}