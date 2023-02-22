package com.company.dto;

public class Cruiser extends ShipAbstract {
    public Cruiser(){
        this.pieces = 3; // with and height
        this.type = "CA";

        coordinates.add(new Coordinate(0, 1));
        coordinates.add(new Coordinate(0, 2));
        coordinates.add(new Coordinate(0, 3));
    }
}
