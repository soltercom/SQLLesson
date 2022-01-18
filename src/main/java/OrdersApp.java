import com.zaxxer.hikari.HikariDataSource;
import repository.RepositoryImpl;
import service.OrderServiceImpl;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class OrdersApp {

    private static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://0.0.0.0:5430/orders");
        ds.setUsername("usr");
        ds.setPassword("pwd");
        return ds;
    }

    public static void main(String[] args) {

        var ds = createDataSource();
        var repository = new RepositoryImpl(ds);
        var orderService = new OrderServiceImpl(repository);

        var orderId = orderService.create("Computers");
        orderService.addItem(orderId, "Computer", 2, BigDecimal.valueOf(150000L));
        orderService.addItem(orderId, "Mouse", 2, BigDecimal.valueOf(1500L));
        orderService.addItem(orderId, "Display", 1, BigDecimal.valueOf(25000L));

        var orderId2 = orderService.create("Net equipment");
        var order2ItemId = orderService.addItem(orderId2, "Router", 20, BigDecimal.valueOf(5000L));

        System.out.println("===========");
        orderService.displayOrder(orderId);
        System.out.println("===========");
        orderService.displayOrder(orderId2);
        System.out.println("===========");

        System.out.println();
        System.out.println("CHANGE COUNT");
        orderService.changeCount(orderId2, order2ItemId, 19);
        System.out.println("===========");
        orderService.displayOrder(orderId2);
        System.out.println("===========");

        System.out.println();
        System.out.println("COMPLETE ORDERS");
        orderService.completeOrders();
        System.out.println("===========");
        orderService.displayOrder(orderId);
        System.out.println("===========");
        orderService.displayOrder(orderId2);
        System.out.println("===========");

    }
}
