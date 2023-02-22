package com.company.dto;

public class Destroyer extends ShipAbstract {
    public Destroyer(){
        this.pieces = 2; // with and height
        this.type = "DD";

        coordinates.add(new Coordinate(0, 0));
        coordinates.add(new Coordinate(1, 0));
    }

}
