package repositories;

import models.User;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * 用户Repository
 */
@Singleton
public class UserRepository {

    private final JPAApi jpaApi;

    @Inject
    public UserRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    private EntityManager em() {
        return jpaApi.em();
    }

    public User save(User user) {
        if (user.getId() == null) {
            em().persist(user);
            return user;
        } else {
            return em().merge(user);
        }
    }

    public Optional<User> findById(Long id) {
        User user = em().find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = em().createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = em().createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findAll(int page, int size) {
        return em().createQuery("SELECT u FROM User u ORDER BY u.id", User.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long count() {
        return em().createQuery("SELECT COUNT(u) FROM User u", Long.class)
                .getSingleResult();
    }

    public List<User> searchByKeyword(String keyword, int page, int size) {
        return em().createQuery(
                "SELECT u FROM User u WHERE u.username LIKE :keyword OR u.email LIKE :keyword OR u.realName LIKE :keyword ORDER BY u.id",
                User.class)
                .setParameter("keyword", "%" + keyword + "%")
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countByKeyword(String keyword) {
        return em().createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username LIKE :keyword OR u.email LIKE :keyword OR u.realName LIKE :keyword",
                Long.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getSingleResult();
    }

    public List<User> findByEnabled(Boolean enabled, int page, int size) {
        return em().createQuery(
                "SELECT u FROM User u WHERE u.enabled = :enabled ORDER BY u.id", User.class)
                .setParameter("enabled", enabled)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countByEnabled(Boolean enabled) {
        return em().createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.enabled = :enabled", Long.class)
                .setParameter("enabled", enabled)
                .getSingleResult();
    }

    public void delete(User user) {
        em().remove(em().contains(user) ? user : em().merge(user));
    }

    public boolean existsByUsername(String username) {
        Long count = em().createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    public boolean existsByEmail(String email) {
        Long count = em().createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}
