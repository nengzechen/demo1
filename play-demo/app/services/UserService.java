package services;

import dto.PageResponse;
import dto.UserRequest;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;
import models.Role;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.jpa.JPAApi;
import repositories.RoleRepository;
import repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户Service
 */
@Singleton
public class UserService {

    private static final Logger.ALogger logger = Logger.of(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JPAApi jpaApi;

    @Inject
    public UserService(UserRepository userRepository, RoleRepository roleRepository, JPAApi jpaApi) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jpaApi = jpaApi;
    }

    public User createUser(UserRequest request) {
        return jpaApi.withTransaction(em -> {
            logger.info("创建用户: {}", request.getUsername());

            if (userRepository.existsByUsername(request.getUsername())) {
                throw new ResourceAlreadyExistsException("用户", "username", request.getUsername());
            }

            if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("用户", "email", request.getEmail());
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setRealName(request.getRealName());

            if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (Long roleId : request.getRoleIds()) {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new ResourceNotFoundException("角色", "id", roleId));
                    roles.add(role);
                }
                user.setRoles(roles);
            }

            return userRepository.save(user);
        });
    }

    public User getUserById(Long id) {
        return jpaApi.withTransaction(em -> {
            logger.info("获取用户: {}", id);
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("用户", "id", id));
        });
    }

    public User getUserByUsername(String username) {
        return jpaApi.withTransaction(em -> {
            logger.info("根据用户名获取用户: {}", username);
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("用户", "username", username));
        });
    }

    public PageResponse<User> getAllUsers(int page, int size) {
        return jpaApi.withTransaction(em -> {
            logger.info("获取所有用户，页码: {}, 页大小: {}", page, size);
            List<User> users = userRepository.findAll(page, size);
            Long total = userRepository.count();
            return new PageResponse<>(users, page, size, total);
        });
    }

    public PageResponse<User> searchUsers(String keyword, int page, int size) {
        return jpaApi.withTransaction(em -> {
            logger.info("搜索用户: {}", keyword);
            List<User> users = userRepository.searchByKeyword(keyword, page, size);
            Long total = userRepository.countByKeyword(keyword);
            return new PageResponse<>(users, page, size, total);
        });
    }

    public PageResponse<User> getUsersByEnabled(Boolean enabled, int page, int size) {
        return jpaApi.withTransaction(em -> {
            logger.info("根据启用状态查询用户: {}", enabled);
            List<User> users = userRepository.findByEnabled(enabled, page, size);
            Long total = userRepository.countByEnabled(enabled);
            return new PageResponse<>(users, page, size, total);
        });
    }

    public User updateUser(Long id, UserRequest request) {
        return jpaApi.withTransaction(em -> {
            logger.info("更新用户: {}", id);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("用户", "id", id));

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            }

            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(request.getEmail())) {
                    throw new ResourceAlreadyExistsException("用户", "email", request.getEmail());
                }
                user.setEmail(request.getEmail());
            }

            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }
            if (request.getRealName() != null) {
                user.setRealName(request.getRealName());
            }
            if (request.getEnabled() != null) {
                user.setEnabled(request.getEnabled());
            }
            if (request.getLocked() != null) {
                user.setLocked(request.getLocked());
            }

            if (request.getRoleIds() != null) {
                Set<Role> roles = new HashSet<>();
                for (Long roleId : request.getRoleIds()) {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new ResourceNotFoundException("角色", "id", roleId));
                    roles.add(role);
                }
                user.setRoles(roles);
            }

            return userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        jpaApi.withTransaction(em -> {
            logger.info("删除用户: {}", id);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("用户", "id", id));
            userRepository.delete(user);
        });
    }

    public void deleteUsers(Set<Long> ids) {
        jpaApi.withTransaction(em -> {
            logger.info("批量删除用户: {}", ids);
            for (Long id : ids) {
                User user = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("用户", "id", id));
                userRepository.delete(user);
            }
        });
    }
}
