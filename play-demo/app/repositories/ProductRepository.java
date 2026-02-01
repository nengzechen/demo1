package repositories;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.PagedList;
import models.Product;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 产品Repository - 演示Ebean ORM的各种查询操作
 */
@Singleton
public class ProductRepository {

    private final EbeanServer ebeanServer;

    public ProductRepository() {
        this.ebeanServer = Ebean.getDefaultServer();
    }

    /**
     * 创建产品
     */
    public Product create(Product product) {
        ebeanServer.save(product);
        return product;
    }

    /**
     * 根据ID查询产品
     */
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(ebeanServer.find(Product.class, id));
    }

    /**
     * 查询所有产品
     */
    public List<Product> findAll() {
        return ebeanServer.find(Product.class).findList();
    }

    /**
     * 分页查询产品
     */
    public PagedList<Product> findPage(int page, int pageSize) {
        return ebeanServer.find(Product.class)
                .setFirstRow(page * pageSize)
                .setMaxRows(pageSize)
                .findPagedList();
    }

    /**
     * 根据分类查询产品
     */
    public List<Product> findByCategory(String category) {
        return ebeanServer.find(Product.class)
                .where()
                .eq("category", category)
                .eq("available", true)
                .findList();
    }

    /**
     * 根据价格范围查询产品
     */
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return ebeanServer.find(Product.class)
                .where()
                .between("price", minPrice, maxPrice)
                .eq("available", true)
                .orderBy("price asc")
                .findList();
    }

    /**
     * 根据名称模糊查询
     */
    public List<Product> searchByName(String keyword) {
        return ebeanServer.find(Product.class)
                .where()
                .ilike("name", "%" + keyword + "%")
                .eq("available", true)
                .findList();
    }

    /**
     * 查询库存不足的产品
     */
    public List<Product> findLowStock(int threshold) {
        return ebeanServer.find(Product.class)
                .where()
                .le("stock", threshold)
                .eq("available", true)
                .findList();
    }

    /**
     * 更新产品
     */
    public Product update(Product product) {
        ebeanServer.update(product);
        return product;
    }

    /**
     * 更新库存
     */
    public boolean updateStock(Long productId, int quantity) {
        Product product = ebeanServer.find(Product.class, productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            ebeanServer.update(product);
            return true;
        }
        return false;
    }

    /**
     * 删除产品（软删除）
     */
    public boolean delete(Long id) {
        Product product = ebeanServer.find(Product.class, id);
        if (product != null) {
            product.setAvailable(false);
            ebeanServer.update(product);
            return true;
        }
        return false;
    }

    /**
     * 物理删除产品
     */
    public boolean hardDelete(Long id) {
        Product product = ebeanServer.find(Product.class, id);
        if (product != null) {
            ebeanServer.delete(product);
            return true;
        }
        return false;
    }

    /**
     * 统计产品总数
     */
    public int count() {
        return ebeanServer.find(Product.class).findCount();
    }

    /**
     * 统计某个分类的产品数量
     */
    public int countByCategory(String category) {
        return ebeanServer.find(Product.class)
                .where()
                .eq("category", category)
                .eq("available", true)
                .findCount();
    }
}
