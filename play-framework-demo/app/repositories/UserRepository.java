package repositories;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.User;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

/**
 * 用户Repository - 演示Ebean ORM操作
 */
@Singleton
public class UserRepository {

    private final EbeanServer ebeanServer;

    public UserRepository() {
        this.ebeanServer = Ebean.getDefaultServer();
    }

    /**
     * 创建用户
     */
    public User create(User user) {
        ebeanServer.save(user);
        return user;
    }

    /**
     * 根据ID查询用户
     */
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(ebeanServer.find(User.class, id));
    }

    /**
     * 根据用户名查询用户
     */
    public Optional<User> findByUsername(String username) {
        return ebeanServer.find(User.class)
                .where()
                .eq("username", username)
                .findOneOrEmpty();
    }

    /**
     * 根据邮箱查询用户
     */
    public Optional<User> findByEmail(String email) {
        return ebeanServer.find(User.class)
                .where()
                .eq("email", email)
                .findOneOrEmpty();
    }

    /**
     * 查询所有用户
     */
    public List<User> findAll() {
        return ebeanServer.find(User.class).findList();
    }

    /**
     * 查询激活的用户
     */
    public List<User> findActiveUsers() {
        return ebeanServer.find(User.class)
                .where()
                .eq("enabled", true)
                .findList();
    }

    /**
     * 更新用户
     */
    public User update(User user) {
        ebeanServer.update(user);
        return user;
    }

    /**
     * 删除用户
     */
    public boolean delete(Long id) {
        User user = ebeanServer.find(User.class, id);
        if (user != null) {
            ebeanServer.delete(user);
            return true;
        }
        return false;
    }

    /**
     * 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        return ebeanServer.find(User.class)
                .where()
                .eq("username", username)
                .findCount() > 0;
    }

    /**
     * 检查邮箱是否存在
     */
    public boolean existsByEmail(String email) {
        return ebeanServer.find(User.class)
                .where()
                .eq("email", email)
                .findCount() > 0;
    }
}
