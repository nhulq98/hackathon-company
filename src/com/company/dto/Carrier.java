package com.company.dto;

public class Carrier extends ShipAbstract{

    public Carrier(){
        this.pieces = 5; // with and height
        this.type = "CV";

        coordinates.add(new Coordinate(0, 0));
        coordinates.add(new Coordinate(1, 0));
    }
}
