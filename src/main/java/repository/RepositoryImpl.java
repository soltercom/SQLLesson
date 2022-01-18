package repository;

import model.Order;
import model.OrderItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RepositoryImpl implements Repository {

    private final DataSource dataSource;

    public RepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long createOrder(String user_name) {
        var sql = "INSERT INTO ORDERING(user_name, done, updated_at) VALUES(?, ?, ?)";

        var id = -1L;

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user_name);
            stmt.setBoolean(2, false);
            stmt.setObject(3, LocalDateTime.now());

            var insertedRows = stmt.executeUpdate();
            final ResultSet keysResultSet = stmt.getGeneratedKeys();
            keysResultSet.next();
            id = keysResultSet.getLong(1);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return id;
    }

    @Override
    public long addItem(long orderId, String name, int count, BigDecimal price) {
        var sql = "INSERT INTO ORDERING_ITEMS(ordering_id, item_name, item_count, item_price) VALUES(?, ?, ?, ?)";
        var sql2 = "UPDATE ORDERING SET updated_at = ? WHERE id = ?";

        var id = -1L;

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             var stmt2 = connection.prepareStatement(sql2)) {

            //connection.setAutoCommit(false);

            stmt.setLong(1, orderId);
            stmt.setString(2, name);
            stmt.setInt(3, count);
            stmt.setObject(4, price);
            stmt.executeUpdate();

            stmt2.setObject(1, LocalDateTime.now());
            stmt2.setLong(2, orderId);
            stmt2.executeUpdate();

            //connection.commit();

            final ResultSet keysResultSet = stmt.getGeneratedKeys();
            keysResultSet.next();
            id = keysResultSet.getLong(1);

        } catch (SQLException ex) {
            /*try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
            System.out.println(ex.getMessage());
        }

        return id;
    }

    @Override
    public void changeCount(long orderId, long orderItemId, int count) {
        var sql = "UPDATE ORDERING_ITEMS SET item_count = ? WHERE ordering_id=? AND id=?";
        var sql2 = "UPDATE ORDERING SET updated_at = ? WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(sql);
             var stmt2 = connection.prepareStatement(sql2)) {

            stmt.setInt(1, count);
            stmt.setLong(2, orderId);
            stmt.setLong(3, orderItemId);
            stmt.executeUpdate();

            stmt2.setObject(1, LocalDateTime.now());
            stmt2.setLong(2, orderId);
            stmt2.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void completeOrders() {
        var sql = "UPDATE ORDERING SET done = TRUE, updated_at = ? WHERE done = FALSE";

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, LocalDateTime.now());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Optional<Order> getOrderById(long orderId) {
        var sql = "SELECT * FROM ORDERING WHERE id = ?";

        Order order = null;

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);

            var resultSet = stmt.executeQuery();
            resultSet.next();

            var id = resultSet.getLong("id");
            var user_name = resultSet.getString("user_name");
            var done = resultSet.getBoolean("done");
            var updated_at = resultSet.getTimestamp("updated_at");

            order = Order.fromDTO(id, user_name, done, updated_at.toLocalDateTime());

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Optional.ofNullable(order);

    }

    @Override
    public List<OrderItem> getOrderItemsList(long orderId) {
        var sql = "SELECT * FROM ORDERING_ITEMS WHERE ordering_id = ?";

        var list = new ArrayList<OrderItem>();

        try (var connection = dataSource.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);

            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                var sqlOrderId = resultSet.getLong("id");
                var id = resultSet.getLong("ordering_id");
                var name = resultSet.getString("item_name");
                var count = resultSet.getInt("item_count");
                var price = resultSet.getBigDecimal("item_price");

                var orderItem = OrderItem.fromDTO(sqlOrderId, id, name, count, price);
                list.add(orderItem);
            }

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Collections.unmodifiableList(list);
    }

}
