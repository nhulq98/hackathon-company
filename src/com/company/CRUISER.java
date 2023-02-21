package com.company;

public class CRUISER  extends ShipAbstract{
    public CRUISER(){
        this.pieces = 3;
        this.type = "CA";

        coordinates.add(new Coordinate(0, 1));
        coordinates.add(new Coordinate(0, 2));
        coordinates.add(new Coordinate(0, 3));
    }
}
