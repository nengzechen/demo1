package services;

import dto.PageResponse;
import dto.RoleRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Permission;
import models.Role;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import play.db.jpa.JPAApi;
import repositories.PermissionRepository;
import repositories.RoleRepository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * RoleService单元测试
 * 使用JPA模拟测试CRUD操作、验证和异常处理
 */
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private JPAApi jpaApi;

    @Mock
    private EntityManager entityManager;

    private RoleService roleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        roleService = new RoleService(roleRepository, permissionRepository, jpaApi);

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
    public void testCreateRole_Success() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Admin");
        request.setCode("ADMIN");
        request.setDescription("Administrator role");

        Role savedRole = new Role("Admin", "ADMIN");
        savedRole.setId(1L);
        savedRole.setDescription("Administrator role");

        when(roleRepository.existsByName("Admin")).thenReturn(false);
        when(roleRepository.existsByCode("ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // When
        Role result = roleService.createRole(request);

        // Then
        assertNotNull(result);
        assertEquals("Admin", result.getName());
        assertEquals("ADMIN", result.getCode());
        assertEquals("Administrator role", result.getDescription());
        assertEquals(Long.valueOf(1L), result.getId());

        verify(roleRepository).existsByName("Admin");
        verify(roleRepository).existsByCode("ADMIN");
        verify(roleRepository).save(any(Role.class));
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreateRole_NameExists() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Admin");
        request.setCode("ADMIN");

        when(roleRepository.existsByName("Admin")).thenReturn(true);

        // When
        roleService.createRole(request);

        // Then - exception should be thrown
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreateRole_CodeExists() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Admin");
        request.setCode("ADMIN");

        when(roleRepository.existsByName("Admin")).thenReturn(false);
        when(roleRepository.existsByCode("ADMIN")).thenReturn(true);

        // When
        roleService.createRole(request);

        // Then - exception should be thrown
    }

    @Test
    public void testCreateRole_WithPermissions() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Manager");
        request.setCode("MANAGER");
        request.setPermissionIds(new HashSet<>(Arrays.asList(1L, 2L)));

        Permission perm1 = new Permission("Create Order", "ORDER:CREATE", "ORDER", "CREATE");
        perm1.setId(1L);
        Permission perm2 = new Permission("Read Order", "ORDER:READ", "ORDER", "READ");
        perm2.setId(2L);

        Role savedRole = new Role("Manager", "MANAGER");
        savedRole.setId(2L);

        when(roleRepository.existsByName("Manager")).thenReturn(false);
        when(roleRepository.existsByCode("MANAGER")).thenReturn(false);
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(perm1));
        when(permissionRepository.findById(2L)).thenReturn(Optional.of(perm2));
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // When
        Role result = roleService.createRole(request);

        // Then
        assertNotNull(result);
        verify(permissionRepository).findById(1L);
        verify(permissionRepository).findById(2L);
        verify(roleRepository).save(any(Role.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreateRole_PermissionNotFound() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Manager");
        request.setCode("MANAGER");
        request.setPermissionIds(new HashSet<>(Arrays.asList(999L)));

        when(roleRepository.existsByName("Manager")).thenReturn(false);
        when(roleRepository.existsByCode("MANAGER")).thenReturn(false);
        when(permissionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        roleService.createRole(request);

        // Then - exception should be thrown
    }

    @Test
    public void testGetRoleById_Success() {
        // Given
        Role role = new Role("Admin", "ADMIN");
        role.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // When
        Role result = roleService.getRoleById(1L);

        // Then
        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Admin", result.getName());
        assertEquals("ADMIN", result.getCode());

        verify(roleRepository).findById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRoleById_NotFound() {
        // Given
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        roleService.getRoleById(999L);

        // Then - exception should be thrown
    }

    @Test
    public void testGetRoleByCode_Success() {
        // Given
        Role role = new Role("Admin", "ADMIN");
        role.setId(1L);

        when(roleRepository.findByCode("ADMIN")).thenReturn(Optional.of(role));

        // When
        Role result = roleService.getRoleByCode("ADMIN");

        // Then
        assertNotNull(result);
        assertEquals("ADMIN", result.getCode());
        assertEquals("Admin", result.getName());

        verify(roleRepository).findByCode("ADMIN");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRoleByCode_NotFound() {
        // Given
        when(roleRepository.findByCode("NONEXISTENT")).thenReturn(Optional.empty());

        // When
        roleService.getRoleByCode("NONEXISTENT");

        // Then - exception should be thrown
    }

    @Test
    public void testGetAllRoles_Pagination() {
        // Given
        List<Role> roles = Arrays.asList(
            new Role("Admin", "ADMIN"),
            new Role("User", "USER")
        );
        roles.get(0).setId(1L);
        roles.get(1).setId(2L);

        when(roleRepository.findAll(0, 10)).thenReturn(roles);
        when(roleRepository.count()).thenReturn(2L);

        // When
        PageResponse<Role> result = roleService.getAllRoles(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(Integer.valueOf(0), result.getPageNumber());
        assertEquals(Integer.valueOf(10), result.getPageSize());
        assertEquals(Long.valueOf(2L), result.getTotalElements());

        verify(roleRepository).findAll(0, 10);
        verify(roleRepository).count();
    }

    @Test
    public void testSearchRoles() {
        // Given
        String keyword = "admin";
        List<Role> roles = Arrays.asList(new Role("Admin", "ADMIN"));
        roles.get(0).setId(1L);

        when(roleRepository.searchByKeyword(keyword, 0, 10)).thenReturn(roles);
        when(roleRepository.countByKeyword(keyword)).thenReturn(1L);

        // When
        PageResponse<Role> result = roleService.searchRoles(keyword, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Admin", result.getContent().get(0).getName());
        assertEquals(Long.valueOf(1L), result.getTotalElements());

        verify(roleRepository).searchByKeyword(keyword, 0, 10);
        verify(roleRepository).countByKeyword(keyword);
    }

    @Test
    public void testUpdateRole_Success() {
        // Given
        Role existingRole = new Role("Admin", "ADMIN");
        existingRole.setId(1L);
        existingRole.setDescription("Old description");

        RoleRequest request = new RoleRequest();
        request.setName("Administrator");
        request.setDescription("New description");
        request.setEnabled(true);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.existsByName("Administrator")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        // When
        Role result = roleService.updateRole(1L, request);

        // Then
        assertNotNull(result);
        verify(roleRepository).findById(1L);
        verify(roleRepository).existsByName("Administrator");
        verify(roleRepository).save(any(Role.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateRole_NotFound() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Updated Name");

        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        roleService.updateRole(999L, request);

        // Then - exception should be thrown
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testUpdateRole_NameConflict() {
        // Given
        Role existingRole = new Role("Admin", "ADMIN");
        existingRole.setId(1L);

        RoleRequest request = new RoleRequest();
        request.setName("User");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.existsByName("User")).thenReturn(true);

        // When
        roleService.updateRole(1L, request);

        // Then - exception should be thrown
    }

    @Test
    public void testUpdateRole_SameNameNoConflict() {
        // Given
        Role existingRole = new Role("Admin", "ADMIN");
        existingRole.setId(1L);

        RoleRequest request = new RoleRequest();
        request.setName("Admin"); // Same name, should not check for conflict
        request.setDescription("Updated description");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        // When
        Role result = roleService.updateRole(1L, request);

        // Then
        assertNotNull(result);
        verify(roleRepository).findById(1L);
        verify(roleRepository, never()).existsByName(anyString());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    public void testUpdateRole_WithPermissions() {
        // Given
        Role existingRole = new Role("Manager", "MANAGER");
        existingRole.setId(2L);

        Permission perm1 = new Permission("Create", "CREATE", "ORDER", "CREATE");
        perm1.setId(1L);

        RoleRequest request = new RoleRequest();
        request.setDescription("Updated");
        request.setPermissionIds(new HashSet<>(Arrays.asList(1L)));

        when(roleRepository.findById(2L)).thenReturn(Optional.of(existingRole));
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(perm1));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        // When
        Role result = roleService.updateRole(2L, request);

        // Then
        assertNotNull(result);
        verify(permissionRepository).findById(1L);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    public void testDeleteRole_Success() {
        // Given
        Role role = new Role("Admin", "ADMIN");
        role.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).delete(role);

        // When
        roleService.deleteRole(1L);

        // Then
        verify(roleRepository).findById(1L);
        verify(roleRepository).delete(role);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteRole_NotFound() {
        // Given
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        roleService.deleteRole(999L);

        // Then - exception should be thrown
    }

    @Test
    public void testDeleteRoles_Multiple() {
        // Given
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));

        Role role1 = new Role("Admin", "ADMIN");
        role1.setId(1L);
        Role role2 = new Role("User", "USER");
        role2.setId(2L);
        Role role3 = new Role("Guest", "GUEST");
        role3.setId(3L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role2));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(role3));
        doNothing().when(roleRepository).delete(any(Role.class));

        // When
        roleService.deleteRoles(ids);

        // Then
        verify(roleRepository).findById(1L);
        verify(roleRepository).findById(2L);
        verify(roleRepository).findById(3L);
        verify(roleRepository, times(3)).delete(any(Role.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteRoles_OneNotFound() {
        // Given
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 999L));

        Role role1 = new Role("Admin", "ADMIN");
        role1.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        roleService.deleteRoles(ids);

        // Then - exception should be thrown
    }

    @Test
    public void testCreateRole_NullPermissionIds() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Basic");
        request.setCode("BASIC");
        request.setPermissionIds(null); // Null permission IDs

        Role savedRole = new Role("Basic", "BASIC");
        savedRole.setId(3L);

        when(roleRepository.existsByName("Basic")).thenReturn(false);
        when(roleRepository.existsByCode("BASIC")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // When
        Role result = roleService.createRole(request);

        // Then
        assertNotNull(result);
        verify(roleRepository).save(any(Role.class));
        verify(permissionRepository, never()).findById(anyLong());
    }

    @Test
    public void testCreateRole_EmptyPermissionIds() {
        // Given
        RoleRequest request = new RoleRequest();
        request.setName("Basic");
        request.setCode("BASIC");
        request.setPermissionIds(new HashSet<>()); // Empty set

        Role savedRole = new Role("Basic", "BASIC");
        savedRole.setId(3L);

        when(roleRepository.existsByName("Basic")).thenReturn(false);
        when(roleRepository.existsByCode("BASIC")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // When
        Role result = roleService.createRole(request);

        // Then
        assertNotNull(result);
        verify(roleRepository).save(any(Role.class));
        verify(permissionRepository, never()).findById(anyLong());
    }

    @Test
    public void testUpdateRole_OnlyDescription() {
        // Given
        Role existingRole = new Role("Admin", "ADMIN");
        existingRole.setId(1L);

        RoleRequest request = new RoleRequest();
        request.setDescription("Only update description");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        // When
        Role result = roleService.updateRole(1L, request);

        // Then
        assertNotNull(result);
        verify(roleRepository, never()).existsByName(anyString());
    }

    @Test
    public void testUpdateRole_OnlyEnabled() {
        // Given
        Role existingRole = new Role("Admin", "ADMIN");
        existingRole.setId(1L);
        existingRole.setEnabled(true);

        RoleRequest request = new RoleRequest();
        request.setEnabled(false);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        // When
        Role result = roleService.updateRole(1L, request);

        // Then
        assertNotNull(result);
        verify(roleRepository).save(any(Role.class));
    }
}
