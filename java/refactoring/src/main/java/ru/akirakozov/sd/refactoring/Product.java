package ru.akirakozov.sd.refactoring;

public class Product {
    private final String name;
    private final int price;
    private final long id;

    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String ID = "id";

    public Product(String name, int price, long id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public long getId() {
        return id;
    }
}
