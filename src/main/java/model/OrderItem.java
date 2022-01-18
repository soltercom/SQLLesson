package model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class OrderItem {

    private final long id;
    private final long orderId;
    private final String name;
    private final int count;
    private final BigDecimal price;

    private OrderItem(long id, long orderId, String name, int count, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public static OrderItem fromDTO(long id, long orderId, String name, int count, BigDecimal price) {
        return new OrderItem(id, orderId, name, count, price);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem orderItem = (OrderItem) o;
        return getId() == orderItem.getId() && getOrderId() == orderItem.getOrderId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderId());
    }
}
