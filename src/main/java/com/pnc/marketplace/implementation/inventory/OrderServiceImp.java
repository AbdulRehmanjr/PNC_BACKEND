package com.pnc.marketplace.implementation.inventory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.inventory.OrderRepository;
import com.pnc.marketplace.database.seller.OrderSellerRepository;
import com.pnc.marketplace.model.Inventory.CartItem;
import com.pnc.marketplace.model.Inventory.Order;
import com.pnc.marketplace.model.seller.OrderSeller;
import com.pnc.marketplace.service.inventory.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderSellerRepository orderSellerRepository;

    @Override
    public Order createOrder(Order order) {

        String code = this.generateCode(8);
        order.setOrderCode(code);

        Order response = this.orderRepo.save(order);

        Set<Integer> uniqueSellerIds = order.getCart().getCartItems().stream()
                .map(CartItem::getSellerId)
                .collect(Collectors.toSet());

        for (Integer sellerId : uniqueSellerIds) {
            OrderSeller orderSeller = new OrderSeller();
            orderSeller.setOrderCode(code);
            orderSeller.setSellerId(sellerId);
            this.orderSellerRepository.save(orderSeller);
        }

        if (response == null)
            return null;

        log.info("Order saved successfully");
        return response;
    }

    @Override
    public Order getOrderById(long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderById'");
    }

    @Override
    public List<Order> getAllOrderByUserEmail(String email) {

        List<Order> response = this.orderRepo.findAllByCustomerEmail(email);

        if (response != null)
            return response;

        log.info("No order Found");
        return null;
    }

    @Override
    public Order getOrderByCode(String orderCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderByCode'");
    }

    /**
     * The function retrieves a list of orders associated with a specific seller by querying the
     * orderSellerRepository for orders with the given sellerId, and then retrieving the corresponding
     * orders from the orderRepo using the order codes obtained from the previous step.
     * 
     * @param sellerId The sellerId parameter is an integer that represents the ID of the seller for
     * whom we want to retrieve the orders.
     * @return The method is returning a list of Order objects.
     */
    @Override
    public List<Order> getOrdersBySeller(int sellerId) {

        List<OrderSeller> sellerOrder = this.orderSellerRepository.findAllBySellerId(sellerId);

        if (sellerOrder == null)
            return null;

        List<String> orderCodes = sellerOrder
                .stream()
                .map(OrderSeller::getOrderCode)
                .collect(Collectors.toList());

        List<Order> orders = new ArrayList<>();

        for (String orderCode : orderCodes) {
            Order order = this.orderRepo.findByOrderCode(orderCode);

            if (order != null) {
                orders.add(order);
            }
        }
        return orders;
    }

    private String generateCode(int length) {
        final SecureRandom RANDOM = new SecureRandom();
        final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(ALPHANUMERIC_STRING.charAt(RANDOM.nextInt(ALPHANUMERIC_STRING.length())));
        }
        return builder.toString();
    }
}
