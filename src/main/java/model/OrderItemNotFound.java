package model;

public class OrderItemNotFound extends RuntimeException {

    private final long id;

    public OrderItemNotFound(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Order item with id " + id + " not found";
    }

}
