package service;

import model.OrderNotFound;
import repository.Repository;
import repository.RepositoryImpl;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderServiceImpl implements OrderService {

    private final Repository repository;

    public OrderServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public long create(String user_name) {
        Objects.requireNonNull(user_name);
        if (user_name.isEmpty()) throw new RuntimeException("User should has name");
        return repository.createOrder(user_name);
    }

    public long addItem(long orderId, String name, int count, BigDecimal price) {
        if (name.isEmpty()) throw new RuntimeException("Item should has a name");
        return repository.addItem(orderId, name, count, price);
    }

    @Override
    public void changeCount(long orderId, long orderItemId, int count) {
        repository.changeCount(orderId, orderItemId, count);
    }

    @Override
    public void displayOrder(long orderId) {
        var order = repository.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
        var orderItemList = repository.getOrderItemsList(orderId);

        System.out.println(order);
        System.out.println("Items:");
        orderItemList.forEach(System.out::println);
    }

    @Override
    public void completeOrders() {
        repository.completeOrders();
    }

}
