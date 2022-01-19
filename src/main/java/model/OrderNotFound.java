package model;

public class OrderNotFound extends RuntimeException {

    private final long id;

    public OrderNotFound(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Order with id " + id + " not found.";
    }

}
