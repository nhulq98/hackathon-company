package com.company.dto;

public class OilRig extends ShipAbstract {

    public OilRig(){
        this.pieces = 2; // with and height
        this.type = "OR";

        coordinates.add(new Coordinate(0, 0));
        coordinates.add(new Coordinate(1, 0));
    }
}
