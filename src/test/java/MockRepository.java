import model.Order;
import model.OrderItem;
import repository.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class MockRepository implements Repository {

    private final Map<Long, Order> orderMap;
    private final Map<Long, Map<Long, OrderItem>> orderItemMap;

    public MockRepository() {
        orderMap = new HashMap<>();
        orderItemMap = new HashMap<>();
    }

    @Override
    public long createOrder(String user_name) {
        var order = Order.fromDTO(
                orderMap.size() + 1,
                user_name, false, LocalDateTime.now());
        orderMap.put(order.getId(), order);
        return order.getId();
    }

    @Override
    public long addItem(long orderId, String name, int count, BigDecimal price) {
        var map = orderItemMap.getOrDefault(orderId, new HashMap<>());
        var orderItem = OrderItem
                .fromDTO(map.size() + 1, orderId, name, count, price);
        map.put(orderItem.getId(), orderItem);
        orderItemMap.put(orderId, map);
        return orderItem.getId();
    }

    @Override
    public void changeCount(long orderId, long orderItemId, int count) {
        var map = orderItemMap.get(orderId);
        if (map != null && map.get(orderItemId) != null) {
            var orderItem = map.get(orderItemId);
            map.put(orderItemId, OrderItem
                .fromDTO(orderItem.getId(), orderItem.getOrderId(), orderItem.getName(), count, orderItem.getPrice()));
        }
    }

    @Override
    public void completeOrders() {
        for (var entrySet: orderMap.entrySet()) {
            var order = entrySet.getValue();
            if (!order.isDone()) {
                entrySet.setValue(Order.fromDTO(order.getId(),
                        order.getUser_name(),
                        true,
                        order.getUpdated_at()));
            }
        }
    }

    @Override
    public Optional<Order> getOrderById(long orderId) {
        return Optional.ofNullable(orderMap.get(orderId));
    }

    @Override
    public List<OrderItem> getOrderItemsList(long orderId) {
        var map = orderItemMap.get(orderId);
        if (map == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(map.values());
    }
}
