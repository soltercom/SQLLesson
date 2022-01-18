package service;

import model.Order;
import model.OrderItem;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderService {

    long create(String user_name);
    long addItem(long orderId, String name, int count, BigDecimal price);
    void changeCount(long orderId, long orderItemId, int count);
    void displayOrder(long orderId);
    void completeOrders();

}
