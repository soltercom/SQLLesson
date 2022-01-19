import model.Order;
import model.OrderNotFound;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.RepositoryImpl;
import service.OrderService;
import service.OrderServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceImplTest {

    private final OrderService orderService;
    private final Repository repository;

    OrderServiceImplTest() {
        // some experiment
        //var ds = OrdersApp.createDataSource();
        //repository = new RepositoryImpl(ds);

        repository = new MockRepository();
        orderService = new OrderServiceImpl(repository);
    }

    private Order getOrderById(long orderId) {
        return repository.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
    }

    private long addOrder(String name) {
        return orderService.create(name);
    }

    private long addOrderItem(long orderId, String name) {
        return orderService.addItem(orderId, name, 1, BigDecimal.ONE);
    }

    @Test
    public void createTest() {
        var name = "Test order";
        var id = addOrder(name);

        assertEquals(1L, id);

        var order = getOrderById(id);
        assertEquals(id, order.getId());
        assertEquals(name, order.getUser_name());
    }

    @Test
    public void createWithEmptyNameTest() {
        var exception = assertThrows(RuntimeException.class, () -> orderService.create(""));

        assertEquals("User should has name", exception.getMessage());
    }

    @Test
    public void addItemTest() {
        var orderId = addOrder("Test Order");
        var name = "Order 1, Item 1";
        var id = addOrderItem(orderId, name);

        assertEquals(1L, id);
    }

    @Test
    public void changeCountTest() {

        var count = 2;

        var orderId = addOrder("Test Order");
        var name = "Order 1, Item 1";
        var id = addOrderItem(orderId, name);

        orderService.changeCount(orderId, id, count);
        var list = repository.getOrderItemsList(orderId);
        var result = list.stream().filter(item -> item.getOrderId() == orderId).anyMatch(item -> item.getCount() == count);

        assertTrue(result);
    }

    @Test
    public void displayOrderTest() {
        var orderId = addOrder("Test Order");
        addOrderItem(orderId, "Item 1");
        addOrderItem(orderId, "Item 2");
        addOrderItem(orderId, "Item 3");

        var list = repository.getOrderItemsList(orderId);
        assertEquals(3, list.size());
    }

    @Test
    public void completeOrdersTest() {
        var orderId1 = addOrder("Test Order 1");
        var orderId2 = addOrder("Test Order 2");

        orderService.completeOrders();

        var order1 = repository.getOrderById(orderId1).orElseThrow(() -> new OrderNotFound(orderId1));
        var order2 = repository.getOrderById(orderId2).orElseThrow(() -> new OrderNotFound(orderId2));

        assertTrue(order1.isDone());
        assertTrue(order2.isDone());
    }

}
