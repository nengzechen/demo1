package services;

import dto.PageResponse;
import dto.PermissionRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Permission;
import play.Logger;
import play.db.jpa.JPAApi;
import repositories.PermissionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

@Singleton
public class PermissionService {

    private static final Logger.ALogger logger = Logger.of(PermissionService.class);
    private final PermissionRepository permissionRepository;
    private final JPAApi jpaApi;

    @Inject
    public PermissionService(PermissionRepository permissionRepository, JPAApi jpaApi) {
        this.permissionRepository = permissionRepository;
        this.jpaApi = jpaApi;
    }

    public Permission createPermission(PermissionRequest request) {
        return jpaApi.withTransaction(em -> {
            logger.info("创建权限: {}", request.getName());

            if (permissionRepository.existsByCode(request.getCode())) {
                throw new ResourceAlreadyExistsException("权限", "code", request.getCode());
            }

            Permission permission = new Permission();
            permission.setName(request.getName());
            permission.setCode(request.getCode());
            permission.setDescription(request.getDescription());
            permission.setResource(request.getResource());
            permission.setAction(request.getAction());

            return permissionRepository.save(permission);
        });
    }

    public Permission getPermissionById(Long id) {
        return jpaApi.withTransaction(em -> {
            return permissionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("权限", "id", id));
        });
    }

    public Permission getPermissionByCode(String code) {
        return jpaApi.withTransaction(em -> {
            return permissionRepository.findByCode(code)
                    .orElseThrow(() -> new ResourceNotFoundException("权限", "code", code));
        });
    }

    public PageResponse<Permission> getAllPermissions(int page, int size) {
        return jpaApi.withTransaction(em -> {
            List<Permission> permissions = permissionRepository.findAll(page, size);
            Long total = permissionRepository.count();
            return new PageResponse<>(permissions, page, size, total);
        });
    }

    public PageResponse<Permission> searchPermissions(String keyword, int page, int size) {
        return jpaApi.withTransaction(em -> {
            List<Permission> permissions = permissionRepository.searchByKeyword(keyword, page, size);
            Long total = permissionRepository.countByKeyword(keyword);
            return new PageResponse<>(permissions, page, size, total);
        });
    }

    public PageResponse<Permission> getPermissionsByResource(String resource, int page, int size) {
        return jpaApi.withTransaction(em -> {
            List<Permission> permissions = permissionRepository.findByResource(resource, page, size);
            Long total = permissionRepository.countByResource(resource);
            return new PageResponse<>(permissions, page, size, total);
        });
    }

    public Permission updatePermission(Long id, PermissionRequest request) {
        return jpaApi.withTransaction(em -> {
            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("权限", "id", id));

            if (request.getName() != null) {
                permission.setName(request.getName());
            }
            if (request.getDescription() != null) {
                permission.setDescription(request.getDescription());
            }
            if (request.getResource() != null) {
                permission.setResource(request.getResource());
            }
            if (request.getAction() != null) {
                permission.setAction(request.getAction());
            }
            if (request.getEnabled() != null) {
                permission.setEnabled(request.getEnabled());
            }

            return permissionRepository.save(permission);
        });
    }

    public void deletePermission(Long id) {
        jpaApi.withTransaction(em -> {
            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("权限", "id", id));
            permissionRepository.delete(permission);
        });
    }

    public void deletePermissions(Set<Long> ids) {
        jpaApi.withTransaction(em -> {
            for (Long id : ids) {
                Permission permission = permissionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("权限", "id", id));
                permissionRepository.delete(permission);
            }
        });
    }
}
