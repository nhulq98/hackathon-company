package com.company.dto;

public class BattleShip extends ShipAbstract {
    public BattleShip(){
        this.pieces = 4; // with and height
        this.type = "BB";

        coordinates.add(new Coordinate(0, 0));
        coordinates.add(new Coordinate(1, 0));
    }
}
