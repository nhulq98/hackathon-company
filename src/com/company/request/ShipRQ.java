package com.company.request;

public class ShipRQ {
    public String type;
    public int quantity;

    public ShipRQ(){}

    public ShipRQ(String type, int quantity){
        this.type = type;
        this.quantity = quantity;
    }
}
