package com.example.TechShop.service;

import com.example.TechShop.dto.request.CartItemRequest;
import com.example.TechShop.dto.response.CartResponse;
import com.example.TechShop.entity.Cart;
import com.example.TechShop.entity.CartItem;
import com.example.TechShop.entity.ProductVariant;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.CartRepository;
import com.example.TechShop.repository.ProductVariantRepository;
import com.example.TechShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    public final CartRepository cartRepository;
    public final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addToCart(Long userId, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(
                            userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
                    return cartRepository.save(newCart);
                });

        ProductVariant variant = productVariantRepository.findById(request.productVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant", "id", request.productVariantId()));

        //Kiem tra cartItem da ton tai trong gio hang -> cong so luong da co
        Optional<CartItem> existedItem = cart.getCartItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(request.productVariantId()))
                .findFirst();
        int currentQuantity = existedItem
                .map(item -> item.getQuantity() + request.quantity())
                .orElseGet(request::quantity);

        //Kiem tra so luong voi kho hang
        if (currentQuantity > variant.getStockQuantity()) {
            throw new AppException(400, String.format("Sản phẩm '%s' không đủ số lượng để bán", variant.getProduct().getName()));
        }

        //Neu da ton tai san pham trong cart thi cap nhat so luong
        //Neu khong co sp thi them moi trong cart
        if (existedItem.isPresent()) {
            existedItem.get().setQuantity(currentQuantity);
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(currentQuantity)
                    .build();

            cart.addCartItem(cartItem);
        }
    }

    @Transactional
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(
                            userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
                    return cartRepository.save(newCart);
                });
        return CartResponse.from(cart);
    }

    @Transactional
    public void updateCartItemQuantity(Long userId, Long variantId, Integer newQuantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant", "id", variantId));

        if (newQuantity <= 0) {
            removeCartItem(userId, variantId);
            return;
        }

        if (newQuantity > variant.getStockQuantity()) {
            throw new AppException(400, String.format("Sản phẩm '%s' không đủ số lượng để bán", variant.getProduct().getName()));
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "VariantId", variantId));
        cartItem.setQuantity(newQuantity);
    }

    @Transactional
    public void removeCartItem(Long userId, Long variantId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "VariantId", variantId));

        cart.removeCartItem(cartItem);
    }

    @Transactional
    public void clearCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));

        cart.getCartItems().clear();
    }
}
