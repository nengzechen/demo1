package services;

import dto.PageResponse;
import dto.PermissionRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Permission;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import play.db.jpa.JPAApi;
import repositories.PermissionRepository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * PermissionService单元测试
 * 测试权限CRUD和资源查询
 */
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private JPAApi jpaApi;

    @Mock
    private EntityManager entityManager;

    private PermissionService permissionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        permissionService = new PermissionService(permissionRepository, jpaApi);

        // Mock JPA transaction behavior for Function
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, ?> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });

        // Mock JPA transaction behavior for Consumer (void methods)
        doAnswer(invocation -> {
            java.util.function.Consumer<EntityManager> consumer = invocation.getArgument(0);
            consumer.accept(entityManager);
            return null;
        }).when(jpaApi).withTransaction(any(java.util.function.Consumer.class));
    }

    @Test
    public void testCreatePermission_Success() {
        // Given
        PermissionRequest request = new PermissionRequest();
        request.setName("Create Order");
        request.setCode("ORDER:CREATE");
        request.setDescription("Permission to create orders");
        request.setResource("ORDER");
        request.setAction("CREATE");

        Permission savedPermission = new Permission("Create Order", "ORDER:CREATE", "ORDER", "CREATE");
        savedPermission.setId(1L);
        savedPermission.setDescription("Permission to create orders");

        when(permissionRepository.existsByCode("ORDER:CREATE")).thenReturn(false);
        when(permissionRepository.save(any(Permission.class))).thenReturn(savedPermission);

        // When
        Permission result = permissionService.createPermission(request);

        // Then
        assertNotNull(result);
        assertEquals("Create Order", result.getName());
        assertEquals("ORDER:CREATE", result.getCode());
        assertEquals("ORDER", result.getResource());
        assertEquals("CREATE", result.getAction());
        assertEquals(Long.valueOf(1L), result.getId());

        verify(permissionRepository).existsByCode("ORDER:CREATE");
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreatePermission_CodeExists() {
        // Given
        PermissionRequest request = new PermissionRequest();
        request.setName("Create Order");
        request.setCode("ORDER:CREATE");
        request.setResource("ORDER");
        request.setAction("CREATE");

        when(permissionRepository.existsByCode("ORDER:CREATE")).thenReturn(true);

        // When
        permissionService.createPermission(request);

        // Then - exception should be thrown
    }

    @Test
    public void testGetPermissionById_Success() {
        // Given
        Permission permission = new Permission("Read Order", "ORDER:READ", "ORDER", "READ");
        permission.setId(1L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        // When
        Permission result = permissionService.getPermissionById(1L);

        // Then
        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Read Order", result.getName());
        assertEquals("ORDER:READ", result.getCode());

        verify(permissionRepository).findById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetPermissionById_NotFound() {
        // Given
        when(permissionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        permissionService.getPermissionById(999L);

        // Then - exception should be thrown
    }

    @Test
    public void testGetPermissionByCode_Success() {
        // Given
        Permission permission = new Permission("Update Order", "ORDER:UPDATE", "ORDER", "UPDATE");
        permission.setId(2L);

        when(permissionRepository.findByCode("ORDER:UPDATE")).thenReturn(Optional.of(permission));

        // When
        Permission result = permissionService.getPermissionByCode("ORDER:UPDATE");

        // Then
        assertNotNull(result);
        assertEquals("ORDER:UPDATE", result.getCode());
        assertEquals("Update Order", result.getName());

        verify(permissionRepository).findByCode("ORDER:UPDATE");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetPermissionByCode_NotFound() {
        // Given
        when(permissionRepository.findByCode("NONEXISTENT")).thenReturn(Optional.empty());

        // When
        permissionService.getPermissionByCode("NONEXISTENT");

        // Then - exception should be thrown
    }

    @Test
    public void testGetAllPermissions() {
        // Given
        List<Permission> permissions = Arrays.asList(
            new Permission("Create", "ORDER:CREATE", "ORDER", "CREATE"),
            new Permission("Read", "ORDER:READ", "ORDER", "READ")
        );
        permissions.get(0).setId(1L);
        permissions.get(1).setId(2L);

        when(permissionRepository.findAll(0, 10)).thenReturn(permissions);
        when(permissionRepository.count()).thenReturn(2L);

        // When
        PageResponse<Permission> result = permissionService.getAllPermissions(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(Integer.valueOf(0), result.getPageNumber());
        assertEquals(Integer.valueOf(10), result.getPageSize());
        assertEquals(Long.valueOf(2L), result.getTotalElements());

        verify(permissionRepository).findAll(0, 10);
        verify(permissionRepository).count();
    }

    @Test
    public void testSearchPermissions() {
        // Given
        String keyword = "order";
        List<Permission> permissions = Arrays.asList(
            new Permission("Create Order", "ORDER:CREATE", "ORDER", "CREATE")
        );
        permissions.get(0).setId(1L);

        when(permissionRepository.searchByKeyword(keyword, 0, 10)).thenReturn(permissions);
        when(permissionRepository.countByKeyword(keyword)).thenReturn(1L);

        // When
        PageResponse<Permission> result = permissionService.searchPermissions(keyword, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Create Order", result.getContent().get(0).getName());
        assertEquals(Long.valueOf(1L), result.getTotalElements());

        verify(permissionRepository).searchByKeyword(keyword, 0, 10);
        verify(permissionRepository).countByKeyword(keyword);
    }

    @Test
    public void testGetPermissionsByResource() {
        // Given
        String resource = "ORDER";
        List<Permission> permissions = Arrays.asList(
            new Permission("Create Order", "ORDER:CREATE", "ORDER", "CREATE"),
            new Permission("Read Order", "ORDER:READ", "ORDER", "READ"),
            new Permission("Update Order", "ORDER:UPDATE", "ORDER", "UPDATE")
        );
        permissions.get(0).setId(1L);
        permissions.get(1).setId(2L);
        permissions.get(2).setId(3L);

        when(permissionRepository.findByResource(resource, 0, 10)).thenReturn(permissions);
        when(permissionRepository.countByResource(resource)).thenReturn(3L);

        // When
        PageResponse<Permission> result = permissionService.getPermissionsByResource(resource, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(Long.valueOf(3L), result.getTotalElements());

        for (Permission permission : result.getContent()) {
            assertEquals("ORDER", permission.getResource());
        }

        verify(permissionRepository).findByResource(resource, 0, 10);
        verify(permissionRepository).countByResource(resource);
    }

    @Test
    public void testUpdatePermission_Success() {
        // Given
        Permission existingPermission = new Permission("Create", "ORDER:CREATE", "ORDER", "CREATE");
        existingPermission.setId(1L);

        PermissionRequest request = new PermissionRequest();
        request.setName("Create Order Permission");
        request.setDescription("Updated description");
        request.setEnabled(false);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(existingPermission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(existingPermission);

        // When
        Permission result = permissionService.updatePermission(1L, request);

        // Then
        assertNotNull(result);
        verify(permissionRepository).findById(1L);
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdatePermission_NotFound() {
        // Given
        PermissionRequest request = new PermissionRequest();
        request.setName("Updated Name");

        when(permissionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        permissionService.updatePermission(999L, request);

        // Then - exception should be thrown
    }

    @Test
    public void testUpdatePermission_OnlyName() {
        // Given
        Permission existingPermission = new Permission("Old Name", "ORDER:CREATE", "ORDER", "CREATE");
        existingPermission.setId(1L);

        PermissionRequest request = new PermissionRequest();
        request.setName("New Name");

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(existingPermission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(existingPermission);

        // When
        Permission result = permissionService.updatePermission(1L, request);

        // Then
        assertNotNull(result);
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test
    public void testUpdatePermission_ResourceAndAction() {
        // Given
        Permission existingPermission = new Permission("Permission", "PRODUCT:CREATE", "PRODUCT", "CREATE");
        existingPermission.setId(2L);

        PermissionRequest request = new PermissionRequest();
        request.setResource("ORDER");
        request.setAction("UPDATE");

        when(permissionRepository.findById(2L)).thenReturn(Optional.of(existingPermission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(existingPermission);

        // When
        Permission result = permissionService.updatePermission(2L, request);

        // Then
        assertNotNull(result);
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test
    public void testDeletePermission_Success() {
        // Given
        Permission permission = new Permission("Delete", "ORDER:DELETE", "ORDER", "DELETE");
        permission.setId(1L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        doNothing().when(permissionRepository).delete(permission);

        // When
        permissionService.deletePermission(1L);

        // Then
        verify(permissionRepository).findById(1L);
        verify(permissionRepository).delete(permission);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeletePermission_NotFound() {
        // Given
        when(permissionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        permissionService.deletePermission(999L);

        // Then - exception should be thrown
    }

    @Test
    public void testDeletePermissions_Multiple() {
        // Given
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));

        Permission perm1 = new Permission("Create", "ORDER:CREATE", "ORDER", "CREATE");
        perm1.setId(1L);
        Permission perm2 = new Permission("Read", "ORDER:READ", "ORDER", "READ");
        perm2.setId(2L);
        Permission perm3 = new Permission("Update", "ORDER:UPDATE", "ORDER", "UPDATE");
        perm3.setId(3L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(perm1));
        when(permissionRepository.findById(2L)).thenReturn(Optional.of(perm2));
        when(permissionRepository.findById(3L)).thenReturn(Optional.of(perm3));
        doNothing().when(permissionRepository).delete(any(Permission.class));

        // When
        permissionService.deletePermissions(ids);

        // Then
        verify(permissionRepository).findById(1L);
        verify(permissionRepository).findById(2L);
        verify(permissionRepository).findById(3L);
        verify(permissionRepository, times(3)).delete(any(Permission.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeletePermissions_OneNotFound() {
        // Given
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 999L));

        Permission perm1 = new Permission("Create", "ORDER:CREATE", "ORDER", "CREATE");
        perm1.setId(1L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(perm1));
        when(permissionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        permissionService.deletePermissions(ids);

        // Then - exception should be thrown
    }

    @Test
    public void testCreatePermission_AllActions() {
        // Given
        String[] actions = {"CREATE", "READ", "UPDATE", "DELETE", "ALL"};

        for (int i = 0; i < actions.length; i++) {
            PermissionRequest request = new PermissionRequest();
            request.setName("Permission " + i);
            request.setCode("ORDER:" + actions[i]);
            request.setResource("ORDER");
            request.setAction(actions[i]);

            Permission savedPermission = new Permission(
                "Permission " + i, "ORDER:" + actions[i], "ORDER", actions[i]
            );
            savedPermission.setId((long) (i + 1));

            when(permissionRepository.existsByCode("ORDER:" + actions[i])).thenReturn(false);
            when(permissionRepository.save(any(Permission.class))).thenReturn(savedPermission);

            // When
            Permission result = permissionService.createPermission(request);

            // Then
            assertNotNull(result);
            assertEquals(actions[i], result.getAction());
        }
    }

    @Test
    public void testUpdatePermission_AllFields() {
        // Given
        Permission existingPermission = new Permission("Old", "OLD:CREATE", "OLD", "CREATE");
        existingPermission.setId(1L);

        PermissionRequest request = new PermissionRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setResource("NEW");
        request.setAction("UPDATE");
        request.setEnabled(false);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(existingPermission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(existingPermission);

        // When
        Permission result = permissionService.updatePermission(1L, request);

        // Then
        assertNotNull(result);
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test
    public void testGetPermissionsByResource_DifferentResources() {
        // Given
        String[] resources = {"ORDER", "PRODUCT", "USER"};

        for (String resource : resources) {
            List<Permission> permissions = Arrays.asList(
                new Permission("Perm", resource + ":CREATE", resource, "CREATE")
            );
            permissions.get(0).setId(1L);

            when(permissionRepository.findByResource(resource, 0, 10)).thenReturn(permissions);
            when(permissionRepository.countByResource(resource)).thenReturn(1L);

            // When
            PageResponse<Permission> result = permissionService.getPermissionsByResource(resource, 0, 10);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals(resource, result.getContent().get(0).getResource());
        }
    }
}
