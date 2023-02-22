package com.company.dto;

import java.util.ArrayList;
import java.util.List;

public abstract class ShipAbstract extends Coordinate {
    public int pieces;
    public String type;
    List<Coordinate> coordinates = new ArrayList<>();
}
