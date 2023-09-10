package com.example.final_UI_dev.service;
import com.example.final_UI_dev.entity.Cart;
import com.example.final_UI_dev.entity.OrderItem;
import com.example.final_UI_dev.entity.Orders;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class OrdersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Transactional
    public Boolean placeOrder(int userId) {
        Users user = usersRepository.findById(userId).orElse(null);
        List<Cart> cartList = cartRepository.findByUser(user);

        if (cartList.size() != 0) {
            Orders order = new Orders();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("Processing");
            order.setShippingAddress(shippingAddressRepository.findByUserId(userId));

            List<OrderItem> orderItems = new ArrayList<>();

            for (Cart cart : cartList) {
                if (cart.getProduct() != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(productsRepository.findById(cart.getProduct().getProductId()).orElse(null));
                    orderItem.setQuantity(cart.getQuantity());
                    orderItems.add(orderItem);
                }
            }

            order.setOrderItems(orderItems);
            ordersRepository.save(order);

            // Delete all items in the cart for the given user_id
            cartRepository.deleteAllByUser(user);
            return true;
        } else {
            return false;
        }
    }
@Autowired
private OrdersRepository ordersRepository;

    public List<Object> getOrdersByUser(int userId, Pageable pageable) {
        Users user = new Users();
        user.setId(userId);
        List<Orders> ordersList = ordersRepository.findByUser(user, pageable);

        // Sort the orders by order date
        ordersList.sort(Comparator.comparing(Orders::getOrderDate));

        List<Object> orders = new ArrayList<>();
        for (Orders order : ordersList) {
            HashMap<String, Object> orderDetails = new HashMap<>();
            orderDetails.put("orderId", order.getOrderId());
            orderDetails.put("status", order.getStatus());

            // Convert order date to "6th June 2023" format
            LocalDateTime orderDate = order.getOrderDate();
            int day = orderDate.getDayOfMonth();
            String formattedDate = getFormattedDate(orderDate);
            orderDetails.put("orderDate", formattedDate);

            List<HashMap<String, Object>> orderItems = new ArrayList<>();
            Long totalPrice = 0L;

            for (OrderItem orderItem : order.getOrderItems()) {
                HashMap<String, Object> itemDetails = new HashMap<>();
                //itemDetails.put("productId", orderItem.getProduct().getProductId());
                itemDetails.put("productName", orderItem.getProduct().getName());
                itemDetails.put("quantity", orderItem.getQuantity());
                itemDetails.put("price", orderItem.getProduct().getPrice()); // Assuming price is a field in the Product class
                Long itemTotalPrice = orderItem.getQuantity() * orderItem.getProduct().getPrice(); // Calculating total price based on quantity and product price
                itemDetails.put("totalPrice", itemTotalPrice);
                orderItems.add(itemDetails);
                totalPrice += itemTotalPrice;
            }

            orderDetails.put("orderItems", orderItems);
            orderDetails.put("totalPrice", totalPrice);

            orders.add(orderDetails);
        }

        return orders;
    }

    private String getFormattedDate(LocalDateTime dateTime) {
        int day = dateTime.getDayOfMonth();
        String ordinalIndicator = getOrdinalIndicator(day);
        String month = dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = dateTime.getYear();

        return day + ordinalIndicator + " " + month + " " + year;
    }

    private String getOrdinalIndicator(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }

        int lastDigit = day % 10;
        switch (lastDigit) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }




}
