package model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Order {

    private final long id;
    private final String user_name;
    private final boolean done;
    private final LocalDateTime updated_at;

    private Order(long id, String user_name, boolean done, LocalDateTime updated_at) {
        this.id = id;
        this.user_name = user_name;
        this.done = done;
        this.updated_at = updated_at;
    }

    public static Order fromDTO(long id, String user_name, boolean done, LocalDateTime updated_at) {
        return new Order(id, user_name, done, updated_at);
    }

    @Override
    public String toString() {
        return "Order{" +
                "user_name='" + user_name + '\'' +
                ", done=" + done +
                ", updated_at=" + updated_at +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
