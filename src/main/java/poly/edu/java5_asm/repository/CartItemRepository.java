package poly.edu.java5_asm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Cart;
import poly.edu.java5_asm.entity.CartItem;
import poly.edu.java5_asm.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // Tìm tất cả items trong giỏ hàng
    List<CartItem> findByCart(Cart cart);
    
    // Tìm item theo giỏ hàng và sản phẩm
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    
    // Xóa tất cả items của giỏ hàng
    void deleteByCart(Cart cart);
}
