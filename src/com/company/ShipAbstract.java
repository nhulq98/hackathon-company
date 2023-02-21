package com.company;

import java.util.ArrayList;
import java.util.List;

public abstract class ShipAbstract extends Coordinate{
    protected int pieces;
    protected String type;
    List<Coordinate> coordinates = new ArrayList<>();
}
