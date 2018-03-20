package org.app.hackio.rajasthanhackathonprototype;

import android.location.Location;

/**
 * Created by gautam on 20/03/18.
 */

public class Order {

    private double price;
    private int quantity;
    private String OrderType, Crop;
    private boolean transportAvailable;
    private Location location;

    public static final String SELL = "Sell";
    public static final String BUY = "Buy";
    public static final String STORE = "Store";


}
