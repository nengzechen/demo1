package repositories;

import models.Permission;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * 权限Repository
 */
@Singleton
public class PermissionRepository {

    private final JPAApi jpaApi;

    @Inject
    public PermissionRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    private EntityManager em() {
        return jpaApi.em("defaultPersistenceUnit");
    }

    public Permission save(Permission permission) {
        if (permission.getId() == null) {
            em().persist(permission);
            return permission;
        } else {
            return em().merge(permission);
        }
    }

    public Optional<Permission> findById(Long id) {
        Permission permission = em().find(Permission.class, id);
        return Optional.ofNullable(permission);
    }

    public Optional<Permission> findByCode(String code) {
        try {
            Permission permission = em().createQuery(
                    "SELECT p FROM Permission p WHERE p.code = :code", Permission.class)
                    .setParameter("code", code)
                    .getSingleResult();
            return Optional.of(permission);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Permission> findAll(int page, int size) {
        return em().createQuery("SELECT p FROM Permission p ORDER BY p.id", Permission.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long count() {
        return em().createQuery("SELECT COUNT(p) FROM Permission p", Long.class)
                .getSingleResult();
    }

    public List<Permission> searchByKeyword(String keyword, int page, int size) {
        return em().createQuery(
                "SELECT p FROM Permission p WHERE p.name LIKE :keyword OR p.code LIKE :keyword OR p.description LIKE :keyword ORDER BY p.id",
                Permission.class)
                .setParameter("keyword", "%" + keyword + "%")
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countByKeyword(String keyword) {
        return em().createQuery(
                "SELECT COUNT(p) FROM Permission p WHERE p.name LIKE :keyword OR p.code LIKE :keyword OR p.description LIKE :keyword",
                Long.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getSingleResult();
    }

    public List<Permission> findByResource(String resource, int page, int size) {
        return em().createQuery(
                "SELECT p FROM Permission p WHERE p.resource = :resource ORDER BY p.id", Permission.class)
                .setParameter("resource", resource)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countByResource(String resource) {
        return em().createQuery(
                "SELECT COUNT(p) FROM Permission p WHERE p.resource = :resource", Long.class)
                .setParameter("resource", resource)
                .getSingleResult();
    }

    public void delete(Permission permission) {
        em().remove(em().contains(permission) ? permission : em().merge(permission));
    }

    public boolean existsByCode(String code) {
        Long count = em().createQuery(
                "SELECT COUNT(p) FROM Permission p WHERE p.code = :code", Long.class)
                .setParameter("code", code)
                .getSingleResult();
        return count > 0;
    }
}
