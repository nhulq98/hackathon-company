package com.company.response;

import com.company.dto.Coordinate;

import java.util.ArrayList;
import java.util.List;

public abstract class ShipAbstractRS {
    public String type;
    public List<Coordinate> coordinates = new ArrayList<>();
}
