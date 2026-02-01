package services;

import dto.PageResponse;
import dto.RoleRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Permission;
import models.Role;
import play.Logger;
import play.db.jpa.JPAApi;
import repositories.PermissionRepository;
import repositories.RoleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class RoleService {

    private static final Logger.ALogger logger = Logger.of(RoleService.class);
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final JPAApi jpaApi;

    @Inject
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, JPAApi jpaApi) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.jpaApi = jpaApi;
    }

    public Role createRole(RoleRequest request) {
        return jpaApi.withTransaction(em -> {
            logger.info("创建角色: {}", request.getName());

            if (roleRepository.existsByName(request.getName())) {
                throw new ResourceAlreadyExistsException("角色", "name", request.getName());
            }
            if (roleRepository.existsByCode(request.getCode())) {
                throw new ResourceAlreadyExistsException("角色", "code", request.getCode());
            }

            Role role = new Role();
            role.setName(request.getName());
            role.setCode(request.getCode());
            role.setDescription(request.getDescription());

            if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
                Set<Permission> permissions = new HashSet<>();
                for (Long permissionId : request.getPermissionIds()) {
                    Permission permission = permissionRepository.findById(permissionId)
                            .orElseThrow(() -> new ResourceNotFoundException("权限", "id", permissionId));
                    permissions.add(permission);
                }
                role.setPermissions(permissions);
            }

            return roleRepository.save(role);
        });
    }

    public Role getRoleById(Long id) {
        return jpaApi.withTransaction(em -> {
            return roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("角色", "id", id));
        });
    }

    public Role getRoleByCode(String code) {
        return jpaApi.withTransaction(em -> {
            return roleRepository.findByCode(code)
                    .orElseThrow(() -> new ResourceNotFoundException("角色", "code", code));
        });
    }

    public PageResponse<Role> getAllRoles(int page, int size) {
        return jpaApi.withTransaction(em -> {
            List<Role> roles = roleRepository.findAll(page, size);
            Long total = roleRepository.count();
            return new PageResponse<>(roles, page, size, total);
        });
    }

    public PageResponse<Role> searchRoles(String keyword, int page, int size) {
        return jpaApi.withTransaction(em -> {
            List<Role> roles = roleRepository.searchByKeyword(keyword, page, size);
            Long total = roleRepository.countByKeyword(keyword);
            return new PageResponse<>(roles, page, size, total);
        });
    }

    public Role updateRole(Long id, RoleRequest request) {
        return jpaApi.withTransaction(em -> {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("角色", "id", id));

            if (request.getName() != null && !request.getName().equals(role.getName())) {
                if (roleRepository.existsByName(request.getName())) {
                    throw new ResourceAlreadyExistsException("角色", "name", request.getName());
                }
                role.setName(request.getName());
            }

            if (request.getDescription() != null) {
                role.setDescription(request.getDescription());
            }
            if (request.getEnabled() != null) {
                role.setEnabled(request.getEnabled());
            }

            if (request.getPermissionIds() != null) {
                Set<Permission> permissions = new HashSet<>();
                for (Long permissionId : request.getPermissionIds()) {
                    Permission permission = permissionRepository.findById(permissionId)
                            .orElseThrow(() -> new ResourceNotFoundException("权限", "id", permissionId));
                    permissions.add(permission);
                }
                role.setPermissions(permissions);
            }

            return roleRepository.save(role);
        });
    }

    public void deleteRole(Long id) {
        jpaApi.withTransaction(em -> {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("角色", "id", id));
            roleRepository.delete(role);
        });
    }

    public void deleteRoles(Set<Long> ids) {
        jpaApi.withTransaction(em -> {
            for (Long id : ids) {
                Role role = roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("角色", "id", id));
                roleRepository.delete(role);
            }
        });
    }
}
