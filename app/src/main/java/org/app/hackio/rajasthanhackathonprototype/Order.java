package org.app.hackio.rajasthanhackathonprototype;


/**
 * Created by gautam on 20/03/18.
 */

public class Order {

    private double price;
    private int quantity;
    private String OrderType, Crop;
    private boolean transportAvailable;
    private String location;

    public static final String SELL = "Sell";
    public static final String BUY = "Buy";
    public static final String STORE = "Store";

    public Order(double price, int quantity, String orderType, String crop, boolean transportAvailable, String location) {
        this.price = price;
        this.quantity = quantity;
        OrderType = orderType;
        Crop = crop;
        this.transportAvailable = transportAvailable;
        this.location = location;
    }

    public Order(int quantity, String orderType, String crop, boolean transportAvailable, String location) {
        this.price = 0;
        this.quantity = quantity;
        OrderType = orderType;
        Crop = crop;
        this.transportAvailable = transportAvailable;
        this.location = location;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getCrop() {
        return Crop;
    }

    public void setCrop(String crop) {
        Crop = crop;
    }

    public boolean isTransportAvailable() {
        return transportAvailable;
    }

    public void setTransportAvailable(boolean transportAvailable) {
        this.transportAvailable = transportAvailable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}