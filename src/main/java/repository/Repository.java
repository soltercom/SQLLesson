package repository;

import model.Order;
import model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface Repository {

    long createOrder(String user_name);
    long addItem(long orderId, String name, int count, BigDecimal price);
    void changeCount(long orderId, long orderItemId, int count);
    void completeOrders();
    Optional<Order> getOrderById(long orderId);
    List<OrderItem> getOrderItemsList(long orderId);

}
