package com.example.final_UI_dev.controller;

import com.example.final_UI_dev.entity.Cart;
import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.CartRepository;
import com.example.final_UI_dev.repository.TokenRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import com.example.final_UI_dev.service.CartService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    TokenRepository tokenRepository;

    @GetMapping("/getAll")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }
    private String extractUsernameFromToken(String token) {
        // Parse the token and retrieve the claims
        Claims claims = Jwts.parser().setSigningKey("yourSigningKey").parseClaimsJws(token).getBody();

        // Extract the username from the claims
        String username = claims.get("username", String.class);

        return username;
    }



  /*  @GetMapping("/cart/{userId}")
    public ResponseEntity<?> getCartByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.replace("Bearer ", "");
        int id=tokenRepository.findUserIdByToken(token);
        System.out.println(id);

        // Use the token to retrieve the cart from the service
        List<Map<String, Object>> cart = cartService.getCartByUsername(id);

        return ResponseEntity.ok(cart);
    }
    */
  @GetMapping("/{userId}")
  public ResponseEntity<?> getCartByUserId(@PathVariable int userId) {
      List<Map<String, Object>> cart = cartService.getCartByUsername(userId);
      return ResponseEntity.ok(cart);
  }

    @GetMapping("/{userId}/{productId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable int userId,@PathVariable int productId ) {
        List<Map<String, Object>> cart = cartService.getCartByUserIdAndProductId(userId, productId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart( @PathVariable int userId, @PathVariable int productId,@PathVariable int quantity) {
        try {
            cartService.addProductToCart(userId, productId, quantity);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> deleteProductFromCart(@PathVariable int userId, @PathVariable int productId) {
    cartService.deleteProduct(userId,productId);
    return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/totalPrice/{userId}")
    public ResponseEntity<?> getTotalCartPrice(@PathVariable int userId) {
        Map<String,Object> totalCartPrice = cartService.calculateTotalCartPrice(userId);
        return ResponseEntity.ok(totalCartPrice);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart( @PathVariable int userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}/{productId}/{quantity}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable int userId,
            @PathVariable int productId,
            @PathVariable int quantity) {
        try {
            cartService.deleteProductByQuantity(userId, productId, quantity);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/getSizeOfCart/{userId}")
    public ResponseEntity<?> getSizeOfCartById(@PathVariable int userId) {
        int size = cartService.getCartSizeByUserId(userId);
        return ResponseEntity.ok(size);
    }
}













