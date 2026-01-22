package repositories;

import models.Role;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * 角色Repository
 */
@Singleton
public class RoleRepository {

    private final JPAApi jpaApi;

    @Inject
    public RoleRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    private EntityManager em() {
        return jpaApi.em();
    }

    public Role save(Role role) {
        if (role.getId() == null) {
            em().persist(role);
            return role;
        } else {
            return em().merge(role);
        }
    }

    public Optional<Role> findById(Long id) {
        Role role = em().find(Role.class, id);
        return Optional.ofNullable(role);
    }

    public Optional<Role> findByCode(String code) {
        try {
            Role role = em().createQuery(
                    "SELECT r FROM Role r WHERE r.code = :code", Role.class)
                    .setParameter("code", code)
                    .getSingleResult();
            return Optional.of(role);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Role> findByName(String name) {
        try {
            Role role = em().createQuery(
                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(role);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Role> findAll(int page, int size) {
        return em().createQuery("SELECT r FROM Role r ORDER BY r.id", Role.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long count() {
        return em().createQuery("SELECT COUNT(r) FROM Role r", Long.class)
                .getSingleResult();
    }

    public List<Role> searchByKeyword(String keyword, int page, int size) {
        return em().createQuery(
                "SELECT r FROM Role r WHERE r.name LIKE :keyword OR r.code LIKE :keyword OR r.description LIKE :keyword ORDER BY r.id",
                Role.class)
                .setParameter("keyword", "%" + keyword + "%")
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countByKeyword(String keyword) {
        return em().createQuery(
                "SELECT COUNT(r) FROM Role r WHERE r.name LIKE :keyword OR r.code LIKE :keyword OR r.description LIKE :keyword",
                Long.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getSingleResult();
    }

    public void delete(Role role) {
        em().remove(em().contains(role) ? role : em().merge(role));
    }

    public boolean existsByCode(String code) {
        Long count = em().createQuery(
                "SELECT COUNT(r) FROM Role r WHERE r.code = :code", Long.class)
                .setParameter("code", code)
                .getSingleResult();
        return count > 0;
    }

    public boolean existsByName(String name) {
        Long count = em().createQuery(
                "SELECT COUNT(r) FROM Role r WHERE r.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult();
        return count > 0;
    }
}
