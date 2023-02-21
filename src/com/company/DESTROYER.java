package com.company;

import java.util.ArrayList;
import java.util.List;

public class DESTROYER extends ShipAbstract{
    public DESTROYER(){
        this.pieces = 2;
        this.type = "DD";

        coordinates.add(new Coordinate(0, 0));
        coordinates.add(new Coordinate(1, 0));
    }

}
