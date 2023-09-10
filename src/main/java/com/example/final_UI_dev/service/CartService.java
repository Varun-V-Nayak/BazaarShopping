package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.Cart;
import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.CartRepository;
import com.example.final_UI_dev.repository.ProductsRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.*;


@Service
@Transactional
public class  CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductsService productService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductsRepository productsRepository;


    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public List<Map<String, Object>> getCartByUsername(int userId) {
        Users users = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(users);
        Map<String, Map<String, Object>> cartDetailsMap = new HashMap<>();
        for (Cart cart : cartList) {
            if (cart.getProduct() != null) {
                String productName = cart.getProduct().getName();
                if (!cartDetailsMap.containsKey(productName)) {
                    Map<String, Object> cartDetails = new HashMap<>();
                    cartDetails.put("name", productName);
                    cartDetails.put("id",cart.getProduct().getProductId());
                    cartDetails.put("image", cart.getProduct().getImageUrl());
                    cartDetails.put("price", cart.getProduct().getPrice());
                    cartDetails.put("quantity", cart.getQuantity());
                    cartDetails.put("totalPrice", cart.getTotalPrice());
                    cartDetails.put("maxQuantity", cart.getProduct().getStock());
                    cartDetailsMap.put(productName, cartDetails);
                } else {
                    Map<String, Object> cartDetails = cartDetailsMap.get(productName);
                    cartDetails.put("id",cart.getProduct().getProductId());
                    int currentQuantity = (int) cartDetails.get("quantity");
                    int currentMaxQuantity = (int) cartDetails.get("maxQuantity");
                    int newQuantity = currentQuantity + cart.getQuantity();
                    int newMaxQuantity = Math.max(currentMaxQuantity, cart.getProduct().getStock());
                    Long newTotalPrice = (Long) cartDetails.get("totalPrice")+ cart.getTotalPrice();
                    cartDetails.put("quantity", newQuantity);
                    cartDetails.put("maxQuantity", newMaxQuantity);
                    cartDetails.put("totalPrice", newTotalPrice);
                }
            }
        }
        return new ArrayList<>(cartDetailsMap.values());
    }



 /*   public String addProductToCart(int userId, int productId, int quantity) {
        Products product = productService.getProductById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (quantity > product.getStock()) {
            throw new RuntimeException("Insufficient stock");
        }

        Cart cart = new Cart();
        cart.setUser(usersRepository.findById(userId).orElse(null));
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setPrice(product.getPrice());
        Long totalPrice = product.getPrice() * quantity;  //total price
        cart.setTotalPrice(totalPrice);

        // Deduct the ordered quantity from the stock
        int remainingStock = product.getStock() - quantity;
        product.setStock(remainingStock);
        productsRepository.save(product);
        cartRepository.save(cart);
        return "Product added to cart";
        //return ;
    }*/

    public String addProductToCart(int userId, int productId, int quantity) {
        Products product = productService.getProductById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        Users user = usersRepository.findById(userId).orElse(null);
        Products products = productsRepository.findById(productId).orElse(null);
        Cart cart = cartRepository.findByUserAndProduct(user, products);

        if (cart != null) {
            int updatedQuantity = cart.getQuantity() + quantity; //quantity > product.getStock()
            if (product.getStock()==0) {
                throw new RuntimeException("Insufficient stock");
            }

            cart.setQuantity(updatedQuantity);
            Long totalPrice = product.getPrice() * updatedQuantity;
            cart.setTotalPrice(totalPrice);
        } else {
            if (quantity > product.getStock()) {
                throw new RuntimeException("Insufficient stock");
            }

            cart = new Cart();
            cart.setUser(usersRepository.findById(userId).orElse(null));
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            Long totalPrice = product.getPrice() * quantity;
            cart.setTotalPrice(totalPrice);
        }

        // Deduct the ordered quantity from the stock
        int remainingStock = product.getStock() - quantity;
        product.setStock(remainingStock);
        productsRepository.save(product);
        cartRepository.save(cart);
        return "Product added to cart";
    }

    public void deleteProduct(int userId, int productId) {
        Users user = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(user);

        for (Cart cart : cartList) {
            if (cart.getProduct().getProductId() == productId) {
                int refundedQuantity = cart.getQuantity();

                // Retrieve the product
                Products product = cart.getProduct();

                // Increase the stock by refunded quantity
                int updatedStock = product.getStock() + refundedQuantity;
                product.setStock(updatedStock);

                // Update the product in the database
                productsRepository.save(product);

                // Delete the cart entry
                cartRepository.delete(cart);
            }
        }
    }
    public void deleteProductByQuantity(int userId, int productId, int quantity) {
        Users user = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(user);

        for (Cart cart : cartList) {
            if (cart.getProduct().getProductId() == productId) {
                if (cart.getQuantity() <= quantity) {
                    // Retrieve the product
                    Products product = cart.getProduct();

                    // Increase the stock by the refunded quantity
                    int updatedStock = product.getStock() + cart.getQuantity();
                    product.setStock(updatedStock);

                    // Update the product in the database
                    productsRepository.save(product);

                    // Calculate the price to be reduced
                    long pricePerUnit = product.getPrice();
                    long priceToReduce = pricePerUnit * cart.getQuantity();

                    // Reduce the total price in the cart
                    long updatedTotalPrice = cart.getTotalPrice() - priceToReduce;
                    cart.setTotalPrice(updatedTotalPrice);

                    // Delete the cart entry
                    cartRepository.delete(cart);
                } else {
                    // Decrease the cart item quantity by the specified quantity
                    cart.setQuantity(cart.getQuantity() - quantity);

                    // Calculate the price to be reduced
                    long pricePerUnit = cart.getProduct().getPrice();
                    long priceToReduce = pricePerUnit * quantity;

                    // Reduce the total price in the cart
                    long updatedTotalPrice = cart.getTotalPrice() - priceToReduce;
                    cart.setTotalPrice(updatedTotalPrice);

                    cartRepository.save(cart);

                    // Increase the stock by the refunded quantity
                    Products product = cart.getProduct();
                    int updatedStock = product.getStock() + quantity;
                    product.setStock(updatedStock);
                    productsRepository.save(product);
                }
                break; // Exit the loop after processing the matching cart item
            }
        }
    }


    public List<Map<String, Object>> getCartByUserIdAndProductId(int userId, int productId) {
        Users users = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(users);
        Map<String, Map<String, Object>> cartDetailsMap = new HashMap<>();
        for (Cart cart : cartList) {
            if (cart.getProduct().getProductId()==productId) {
                String productName = cart.getProduct().getName();
                if (!cartDetailsMap.containsKey(productName)) {
                    Map<String, Object> cartDetails = new HashMap<>();
                    cartDetails.put("name", productName);
                    cartDetails.put("id",cart.getProduct().getProductId());
                    cartDetails.put("image", cart.getProduct().getImageUrl());
                    cartDetails.put("price", cart.getProduct().getPrice());
                    cartDetails.put("quantity", cart.getQuantity());
                    cartDetails.put("totalPrice", cart.getTotalPrice());
                    cartDetails.put("maxQuantity", cart.getProduct().getStock());
                    cartDetailsMap.put(productName, cartDetails);
                } else {
                    Map<String, Object> cartDetails = cartDetailsMap.get(productName);
                    cartDetails.put("id",cart.getProduct().getProductId());
                    int currentQuantity = (int) cartDetails.get("quantity");
                    int currentMaxQuantity = (int) cartDetails.get("maxQuantity");
                    int newQuantity = currentQuantity + cart.getQuantity();
                    int newMaxQuantity = Math.max(currentMaxQuantity, cart.getProduct().getStock());
                    Long newTotalPrice = (Long) cartDetails.get("totalPrice") + cart.getTotalPrice();
                    cartDetails.put("quantity", newQuantity);
                    cartDetails.put("maxQuantity", newMaxQuantity);
                    cartDetails.put("totalPrice", newTotalPrice);
                }
            }
        }
        return new ArrayList<>(cartDetailsMap.values());
    }
    public Map<String,Object> calculateTotalCartPrice(int userId) {
        Users users = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(users);
        long totalCartPrice = 0;

        for (Cart cart : cartList) {
            System.out.println(cart.getTotalPrice());
            totalCartPrice += cart.getTotalPrice();
        }

        long tax = 40;
        long grandTotal = tax + totalCartPrice;

        if(totalCartPrice ==0){
            grandTotal=0;
            tax = 0;
        }
        Map<String,Object> totalCart = new LinkedHashMap<>();
        totalCart.put("Sub-Total",totalCartPrice);
        totalCart.put("Shipping","-");
        totalCart.put("tax",tax);
        totalCart.put("Discount","-");
        totalCart.put("Total Amount", grandTotal);

        return totalCart;

    }

    public void clearCart(int userId) {
    }


    public int getCartSizeByUserId(int userId) {
        Users users = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(users);
        Map<String, Map<String, Object>> cartDetailsMap = new HashMap<>();
        for (Cart cart : cartList) {
            if (cart.getProduct() != null) {
                String productName = cart.getProduct().getName();
                if (!cartDetailsMap.containsKey(productName)) {
                    Map<String, Object> cartDetails = new HashMap<>();
                    cartDetails.put("name", productName);
                    cartDetails.put("id",cart.getProduct().getProductId());
                    cartDetails.put("image", cart.getProduct().getImageUrl());
                    cartDetails.put("price", cart.getProduct().getPrice());
                    cartDetails.put("quantity", cart.getQuantity());
                    cartDetails.put("totalPrice", cart.getTotalPrice());
                    cartDetails.put("maxQuantity", cart.getProduct().getStock());
                    cartDetailsMap.put(productName, cartDetails);
                } else {
                    Map<String, Object> cartDetails = cartDetailsMap.get(productName);
                    cartDetails.put("id",cart.getProduct().getProductId());
                    int currentQuantity = (int) cartDetails.get("quantity");
                    int currentMaxQuantity = (int) cartDetails.get("maxQuantity");
                    int newQuantity = currentQuantity + cart.getQuantity();
                    int newMaxQuantity = Math.max(currentMaxQuantity, cart.getProduct().getStock());
                    Long newTotalPrice = (Long) cartDetails.get("totalPrice")+ cart.getTotalPrice();
                    cartDetails.put("quantity", newQuantity);
                    cartDetails.put("maxQuantity", newMaxQuantity);
                    cartDetails.put("totalPrice", newTotalPrice);
                }
            }
        }
        return cartDetailsMap.size();
    }
}











