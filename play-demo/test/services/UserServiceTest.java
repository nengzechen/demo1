package services;

import dto.PageResponse;
import dto.UserRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Role;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import play.db.jpa.JPAApi;
import repositories.RoleRepository;
import repositories.UserRepository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.function.Consumer;

/**
 * UserService 单元测试
 */
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JPAApi jpaApi;

    @Mock
    private EntityManager entityManager;

    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, roleRepository, jpaApi);

        // 模拟 JPA 事务 - Function版本（有返回值）
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, ?> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });

        // 模拟 JPA 事务 - Consumer版本（无返回值，用于 delete 操作）
        doAnswer(invocation -> {
            java.util.function.Consumer<EntityManager> consumer = invocation.getArgument(0);
            consumer.accept(entityManager);
            return null;
        }).when(jpaApi).withTransaction(any(java.util.function.Consumer.class));
    }

    @Test
    public void testCreateUser_Success() {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("test@example.com");
        request.setPhone("13800138000");
        request.setRealName("测试用户");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreateUser_UsernameExists() {
        UserRequest request = new UserRequest();
        request.setUsername("existinguser");
        request.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        userService.createUser(request);
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreateUser_EmailExists() {
        UserRequest request = new UserRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setEmail("existing@example.com");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        userService.createUser(request);
    }

    @Test
    public void testCreateUser_WithRoles() {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ADMIN");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role2));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(request);

        assertNotNull(result);
        verify(roleRepository).findById(1L);
        verify(roleRepository).findById(2L);
    }

    @Test
    public void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("testuser", result.getUsername());

        verify(userRepository).findById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        userService.getUserById(999L);
    }

    @Test
    public void testGetUserByUsername_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());

        verify(userRepository).findByUsername("testuser");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        userService.getUserByUsername("nonexistent");
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        users.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        users.add(user2);

        when(userRepository.findAll(0, 10)).thenReturn(users);
        when(userRepository.count()).thenReturn(2L);

        PageResponse<User> result = userService.getAllUsers(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2L, result.getTotalElements().longValue());
        assertEquals(Integer.valueOf(0), result.getPageNumber());
        assertEquals(Integer.valueOf(10), result.getPageSize());

        verify(userRepository).findAll(0, 10);
        verify(userRepository).count();
    }

    @Test
    public void testSearchUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        users.add(user);

        when(userRepository.searchByKeyword("test", 0, 10)).thenReturn(users);
        when(userRepository.countByKeyword("test")).thenReturn(1L);

        PageResponse<User> result = userService.searchUsers("test", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getTotalElements().longValue());

        verify(userRepository).searchByKeyword("test", 0, 10);
        verify(userRepository).countByKeyword("test");
    }

    @Test
    public void testGetUsersByEnabled() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setUsername("enableduser");
        user.setEnabled(true);
        users.add(user);

        when(userRepository.findByEnabled(true, 0, 10)).thenReturn(users);
        when(userRepository.countByEnabled(true)).thenReturn(1L);

        PageResponse<User> result = userService.getUsersByEnabled(true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(userRepository).findByEnabled(true, 0, 10);
        verify(userRepository).countByEnabled(true);
    }

    @Test
    public void testUpdateUser_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setEmail("old@example.com");

        UserRequest request = new UserRequest();
        request.setEmail("new@example.com");
        request.setPhone("13900139000");
        request.setRealName("新名字");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(1L, request);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateUser_NotFound() {
        UserRequest request = new UserRequest();
        request.setEmail("new@example.com");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        userService.updateUser(999L, request);
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testUpdateUser_EmailExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setEmail("old@example.com");

        UserRequest request = new UserRequest();
        request.setEmail("taken@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        userService.updateUser(1L, request);
    }

    @Test
    public void testDeleteUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteUser_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        userService.deleteUser(999L);
    }

    @Test
    public void testDeleteUsers_Success() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        doNothing().when(userRepository).delete(any(User.class));

        userService.deleteUsers(ids);

        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(userRepository, times(2)).delete(any(User.class));
    }
}
