package com.company.dto;

import java.util.ArrayList;
import java.util.List;

public class GameInviteRQ {
    public int boardWidth;
    public int boardHeight;
    public List<ShipRQ> ships;

    public GameInviteRQ(){
        this.boardWidth = 20;
        this.boardHeight = 8;

        //test
        this.ships = new ArrayList<>();
        ships.add(new ShipRQ("CV", 1));
        ships.add(new ShipRQ("BB", 1));
        ships.add(new ShipRQ("OR", 1));
        ships.add(new ShipRQ("CA", 1));
        ships.add(new ShipRQ("DD", 1));
        //-------END---------
    }
}
