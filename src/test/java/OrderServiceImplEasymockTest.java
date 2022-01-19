import model.Order;
import model.OrderItem;
import org.junit.jupiter.api.Test;
import repository.Repository;
import service.OrderService;
import service.OrderServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderServiceImplEasymockTest {

    private final OrderService orderService;
    private final Repository repository;

    OrderServiceImplEasymockTest() {
        repository = createMock(Repository.class);
        orderService = new OrderServiceImpl(repository);
    }

    private Order createOrder(String user_name) {
        return Order.fromDTO(1L, user_name, false, LocalDateTime.now());
    }

    private OrderItem createOrderItem(long orderId, String name) {
        return OrderItem.fromDTO(1L, orderId, name, 1, BigDecimal.ONE);
    }

    @Test
    public void createTest() {
        var expectedReturn = 1L;

        expect(repository.createOrder(anyString()))
                .andReturn(expectedReturn);
        replay(repository);

        var result = orderService.create("Test Order");
        assertEquals(expectedReturn, result);
    }

    @Test
    public void createWithEmptyNameTest() {
        var exception = assertThrows(RuntimeException.class, () -> orderService.create(""));

        assertEquals("User should has name", exception.getMessage());
    }

    @Test
    public void addItemTest() {
        var expectedReturn = 1L;

        expect(repository.addItem(anyLong(), anyString(), anyInt(), isA(BigDecimal.class)))
                .andReturn(expectedReturn);
        replay(repository);

        var result = orderService.addItem(1L, "Item", 1, BigDecimal.ONE);
        assertEquals(expectedReturn, result);
    }

}
