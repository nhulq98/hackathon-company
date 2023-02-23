package com.company;

import com.company.dto.*;
import com.company.response.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class BattleShips2 extends ShipAbstract {
    public static int numRows = 20;
    public static int numCols = 8;
    public static String HORIZON = "HORIZON";
    public static String VERTICAL = "VERTICAL";
    public static int playerShips;
    public static int computerShips;
    public static String[][] grid = new String[numRows][numCols];
    public static List<List<Coordinate>> occupied = new ArrayList<>();

    public static int[][] missedGuesses = new int[numRows][numCols];

    public static void main(String[] args){
        System.out.println("**** Welcome to Battle Ships game ****");
        System.out.println("Right now, sea is empty\n");

        //Step 1 ? Create the ocean map
        createOceanMap();

        //Step 2 ? Deploy player�fs ships
        //Deploying five ships for player
        GameInviteRQ ships = new GameInviteRQ();

        GameStartRS gameStartRS = placeShips(ships);

        deployPlayerShips(gameStartRS);
    }

    public static Coordinate randomCoordinate(){
        int xMin = 0, xMax = 19, yMin = 0, yMax = 7;
        int xRand = 0;
        int yRand = 0;
        do{
            xRand = (int) ((Math.random()) * (xMax - xMin + 1)) + xMin;
            yRand = (int) ((Math.random()) * (yMax - yMin + 1)) + yMin;
        }while (is_overlap_other_ship(xRand, yRand) == true);

        return new Coordinate(xRand, yRand);
    }

    public static List<Coordinate> findCoordinateByShip(ShipAbstractRS ship, int lenOfShip){
        List<Coordinate> coordinates = new ArrayList<>();
        boolean isFindComplete = true; // Find coordinates finish

        do {
            //Step1: Random
            Coordinate rootCoordinate = randomCoordinate();
            coordinates.add(rootCoordinate);

            //Step2: find others Coordinate

            // t?m theo chi?u ngang --> t?ng x
            for (int y = 1; y < lenOfShip; y++) {
                if (!is_overlap_other_ship(rootCoordinate.x, rootCoordinate.y + y) && !is_outside_board(rootCoordinate.x, rootCoordinate.y + y)) {
                    Coordinate coordinateHorizon = new Coordinate(rootCoordinate.x, rootCoordinate.y + y);
                    coordinates.add(coordinateHorizon);
                }else{
                    isFindComplete = false;
                }
            }
            if (isFindComplete == false) { // Tìm theo chiều dọc
                for (int x = 1; x < lenOfShip; x++) {
                    if (!is_overlap_other_ship(rootCoordinate.x + x, rootCoordinate.y) && !is_outside_board(rootCoordinate.x + x, rootCoordinate.y)) {
                        Coordinate coordinateVertical = new Coordinate(rootCoordinate.x + x, rootCoordinate.y);
                        coordinates.add(coordinateVertical);
                        isFindComplete = true;
                    }else{
                        isFindComplete = false;
                    }
                }
            }
        }while(isFindComplete != true);

        return coordinates;
    }


    public static GameStartRS placeShips(GameInviteRQ ships){
        GameStartRS response = new GameStartRS();

        // Generate Coordinate
        ships.ships.forEach(item -> {

            List<Coordinate> coordinates = new ArrayList<>();
            Destroyer destroyerInfo = new Destroyer();
            Cruiser cruiserInfo = new Cruiser();
            OilRig oilRigInfo = new OilRig();
            BattleShip battleShipInfo = new BattleShip();
            Carrier carrierInfo = new Carrier();

            switch (item.type){
                case "DD":{
                    int lenOfShip = destroyerInfo.pieces;

                    for(int i = 0; i < item.quantity; i++){
                        DestroyerRS destroyerRS = new DestroyerRS();
                        destroyerRS.coordinates = findCoordinateByShip(destroyerRS, lenOfShip);

                        //Step3: add Coordinate to resp
                        response.ships.add(destroyerRS);

                        //step4: add to occupied
                        occupied.add(destroyerRS.coordinates);
                    }
                    break;
                }

                case "CA": {
                    int lenOfShip = cruiserInfo.pieces;

                    for(int i = 0; i < item.quantity; i++){
                        CruiserRS cruiserRS = new CruiserRS();

                        cruiserRS.coordinates = findCoordinateByShip(cruiserRS, lenOfShip);

                        //Step3: add Coordinate to resp
                        response.ships.add(cruiserRS);

                        //step4: add to occupied
                        occupied.add(cruiserRS.coordinates);
                    }

                    break;
                }

                case "OR": {
                    int lenOfShip = oilRigInfo.pieces;
                    boolean reRandom = false;

                    for(int z = 0; z < item.quantity; z++){

                        OilRigRS oilRigRS = new OilRigRS();
                        String direction = HORIZON;
                        boolean isFindRootTwoOK = false;
                        boolean isFindComplete = false;

                        do {
                            //Step1: Random find rootCoordinate
                            Coordinate rootCoordinate = randomCoordinate();
                            coordinates.add(rootCoordinate);

                            //Step2: find root 2

                            // t?m theo chi?u ngang --> t?ng x
                            direction = HORIZON; // TÌm điểm tiếp theo. Khởi tạo là chiều ngang
                            if (!is_overlap_other_ship(rootCoordinate.x + 1, rootCoordinate.y)) {
                                Coordinate coordinateHorizon = new Coordinate(rootCoordinate.x + 1, rootCoordinate.y);
                                coordinates.add(coordinateHorizon);
                                isFindRootTwoOK = true;
                            }
                            if(isFindRootTwoOK == false){
                                if (!is_overlap_other_ship(rootCoordinate.x - 1, rootCoordinate.y)) {
                                    Coordinate coordinateHorizon = new Coordinate(rootCoordinate.x - 1, rootCoordinate.y);
                                    coordinates.add(coordinateHorizon);
                                    isFindRootTwoOK = true;
                                }else{
                                    direction = VERTICAL;
                                }
                            }

                            if(direction == VERTICAL){ // tìm điểm tiếp theo theo chiều dọc
                                if (!is_overlap_other_ship(rootCoordinate.x, rootCoordinate.y + 1)) {
                                    Coordinate coordinateVertical = new Coordinate(rootCoordinate.x , rootCoordinate.y + 1);
                                    coordinates.add(coordinateVertical);
                                    isFindRootTwoOK = true;
                                }
                                if(isFindRootTwoOK == false){
                                    if (!is_overlap_other_ship(rootCoordinate.x, rootCoordinate.y - 1)) {
                                        Coordinate coordinateHorizon = new Coordinate(rootCoordinate.x, rootCoordinate.y - 1);
                                        coordinates.add(coordinateHorizon);
                                        isFindRootTwoOK = true;
                                    }
                                }
                            }

                            if(isFindRootTwoOK){

                                // Continue find all coordinate remain
                                boolean turnAround = true;

                                List<Coordinate> remainCoordinates = new ArrayList<>();

                                // Find continue
                                if(direction.equals(HORIZON)){
                                    for(int i = 0; i < coordinates.size(); i++){
                                        Coordinate coordinate = coordinates.get(i);
                                        int x = coordinate.x;
                                        int y = coordinate.y + 1;
                                        if(!is_outside_board(x, y) && !is_overlap_other_ship(x, y)){
                                            turnAround = false;
                                            remainCoordinates.add(new Coordinate(x, y));
                                            continue;
                                        }else{
                                            turnAround = true;
                                            remainCoordinates = new ArrayList<>(); // reset
                                            i = 0;
                                        }

                                        if(turnAround == true){
                                            int x1 = coordinate.x;
                                            int y1 = coordinate.y - 1;
                                            if(!is_outside_board(x1, y1) && !is_overlap_other_ship(x1, y1)){
                                                remainCoordinates.add(new Coordinate(x1, y1));
                                                continue;
                                            }else{ // re-random
                                                reRandom = true;
                                            }
                                        }
                                    }

                                    if(remainCoordinates.size() == 2){ // find OK
                                        coordinates.addAll(remainCoordinates);
                                    }else{
                                        turnAround = true;
                                        remainCoordinates = new ArrayList<>();
                                    }
                                }
                                if(turnAround == true) { // VERTICAL
                                    for(int i = 0; i < coordinates.size(); i++){
                                        Coordinate coordinate = coordinates.get(i);
                                        int x = coordinate.x + 1;
                                        int y = coordinate.y;
                                        if(!is_outside_board(x, y) && !is_overlap_other_ship(x, y)){
                                            turnAround = false;
                                            remainCoordinates.add(new Coordinate(x, y));
                                            continue;
                                        }else{
                                            turnAround = true;
                                            i = 0;
                                        }

                                        if(turnAround == true){

                                            int x1 = coordinate.x - 1;
                                            int y1 = coordinate.y;
                                            if(!is_outside_board(x1, y1) && !is_overlap_other_ship(x1, y1)){
                                                remainCoordinates.add(new Coordinate(x1, y1));
                                                continue;
                                            }else{ // re-random
                                                reRandom = true;
                                            }
                                        }
                                    }
                                    if(remainCoordinates.size() == 2){ // find OK
                                        coordinates.addAll(remainCoordinates);
                                    }else{
                                        reRandom = true;
                                    }
                                }
                            }

                        }while(reRandom == true);

                        oilRigRS.coordinates = coordinates;

                        //Step3: add Coordinate to resp
                        response.ships.add(oilRigRS);

                        //step4: add to occupied
                        occupied.add(coordinates);
                    }

                    break;
                }

                case "BB":{
                    // khởi tạo tàu chiến
                    int lenOfShip = battleShipInfo.pieces;

                    for(int i = 0; i < item.quantity; i++){
                        BattleShipRS battleShipRS = new BattleShipRS();

                        // get Coordinate
                        battleShipRS.coordinates =  findCoordinateByShip(battleShipRS, lenOfShip);;

                        //Step3: add Coordinate to resp
                        response.ships.add(battleShipRS);

                        //step4: add to occupied
                        occupied.add(battleShipRS.coordinates);
                    }
                    break;
                }

                case "CV": {
                    int lenOfShip = carrierInfo.pieces;

                    for(int i = 0; i < item.quantity; i++){
                        CarrierRS carrierRS = new CarrierRS();
                        boolean isFindComplete = true;
                        String direction = HORIZON;
                        do {
                            //Step1: Random
                            Coordinate rootCoordinate = randomCoordinate();
                            coordinates.add(rootCoordinate);

                            //Step2: find others Coordinate

                            // t?m theo chi?u ngang --> t?ng x
                            for (int y = 1; y < lenOfShip; y++) {
                                direction = HORIZON;
                                if (!is_overlap_other_ship(rootCoordinate.x, rootCoordinate.y + y) && !is_outside_board(rootCoordinate.x, rootCoordinate.y + y)) {
                                    Coordinate coordinateHorizon = new Coordinate(rootCoordinate.x, rootCoordinate.y + y);
                                    coordinates.add(coordinateHorizon);
                                }else{
                                    isFindComplete = false;
                                }
                            }
                            if (isFindComplete == false) { // Tìm theo chiều dọc
                                direction = VERTICAL;
                                for (int x = 1; x < lenOfShip; x++) {
                                    if (!is_overlap_other_ship(rootCoordinate.x + x, rootCoordinate.y) && !is_outside_board(rootCoordinate.x + x, rootCoordinate.y)) {
                                        isFindComplete = true;
                                        Coordinate coordinateVertical = new Coordinate(rootCoordinate.x + x, rootCoordinate.y);
                                        coordinates.add(coordinateVertical);
                                    }else{
                                        isFindComplete = false;
                                    }
                                }
                            }
                            boolean isOK = false;
                            // Find continue
                            if(isFindComplete == true){
                                Coordinate coordinate = coordinates.get(1);
                                if(direction.equals(HORIZON)){
                                    int x = coordinate.x;
                                    int y = coordinate.y + 1;
                                    if(!is_outside_board(x, y) && !is_overlap_other_ship(x, y)){
                                        isOK = true;
                                        coordinates.add(new Coordinate(x, y));
                                    }

                                    if(isOK == false){
                                        int x1 = coordinate.x;
                                        int y1 = coordinate.y - 1;
                                        if(!is_outside_board(x1, y1) && !is_overlap_other_ship(x1, y1)){
                                            isOK = true;
                                            coordinates.add(new Coordinate(x1, y1));
                                        }
                                    }

                                }else {
                                    int x = coordinate.x + 1;
                                    int y = coordinate.y;
                                    if(!is_outside_board(x, y) && !is_overlap_other_ship(x, y)){
                                        isOK = true;
                                        coordinates.add(new Coordinate(x, y));
                                    }

                                    if(isOK == false){
                                        int x1 = coordinate.x - 1;
                                        int y1 = coordinate.y;
                                        if(!is_outside_board(x1, y1) && !is_overlap_other_ship(x1, y1)){
                                            isOK = true;
                                            coordinates.add(new Coordinate(x1, y1));
                                        }
                                    }
                                }
                            }
                            if(isOK == false){
                                isFindComplete = false; // re-radom
                            }

                        }while(isFindComplete != true);

                        carrierRS.coordinates = coordinates;

                        //Step3: add Coordinate to resp
                        response.ships.add(carrierRS);

                        //step4: add to occupied
                        occupied.add(coordinates);
                    }

                    break;
                }
                default:
            }

        });

        return response;
    }

    public static void createOceanMap(){
        //First section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        //Middle section of Ocean Map
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = " ";
                if (j == 0)
                    System.out.print(i + "|" + grid[i][j]);
                else if (j == grid[i].length - 1)
                    System.out.print(grid[i][j] + "|" + i);
                else
                    System.out.print(grid[i][j]);
            }
            System.out.println();
        }

        //Last section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }

    public static boolean is_outside_board(int x, int y){
        if((x >= 0 && x < numRows) && (y >= 0 && y < numCols)){
            return false;
        }
        System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        return true;
    }

    public static Boolean is_overlap_other_ship(int x, int y){
        for(int i = 0; i < occupied.size(); i++){
            List<Coordinate> coordinates = occupied.get(i);
            for (int j = 0; j < coordinates.size(); j++) {
                if(coordinates.get(j).x == x && coordinates.get(j).y == y){
                    System.out.println("You can't place two or more ships on the same location (x,y) -> (" + x + ","+ y + ")");
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean get_near_positions(int x, int y){
        return true;
    }


//   not near any ship
//    public static boolean is_near_other_ship(int x, int y){
////        """
////        A position is nice when it does not near any ships.
////        :param x, y: A position need to check
////        :return:
////        """
//        nears_position = BoardHelper.get_near_positions(, width, height)
//        if BoardHelper.is_double_occupied(nears_position, allocates):
//        return True
//        return False
//    }

    public static void deployPlayerShips(GameStartRS gameStartRS){
        System.out.println("\nDeploy your ships:");

        // Get all coordinates and fill to map

        //1: get list ships
        List<ShipAbstractRS> ships = gameStartRS.ships;

        //step2: get all coordinates của ship
        for (int i = 0; i < ships.size(); i++) {
            List<Coordinate> coordinates = ships.get(i).coordinates;

            for (int j = 0; j < coordinates.size(); j++) {
                int x = coordinates.get(j).x;
                int y = coordinates.get(j).y;

                grid[x][y] = "@";
            }
        }
        printOceanMap();
    }

//    public static void deployComputerShips(){
//        System.out.println("\nComputer is deploying ships");
//        //Deploying five ships for computer
//        BattleShips.computerShips = 5;
//        for (int i = 1; i <= BattleShips.computerShips; ) {
//            int x = (int)(Math.random() * 10);
//            int y = (int)(Math.random() * 10);
//
//            if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (grid[x][y] == " "))
//            {
//                grid[x][y] =   "x";
//                System.out.println(i + ". ship DEPLOYED");
//                i++;
//            }
//        }
//        printOceanMap();
//    }

//    public static void Battle(){
//        printOceanMap();
//
//        System.out.println();
//        System.out.println("Your ships: " + BattleShips.playerShips + " | Computer ships: " + BattleShips.computerShips);
//        System.out.println();
//    }
//    public static void gameOver(){
//        System.out.println("Your ships: " + BattleShips.playerShips + " | Computer ships: " + BattleShips.computerShips);
//        if(BattleShips.playerShips > 0 && BattleShips.computerShips <= 0)
//            System.out.println("Hooray! You won the battle :)");
//        else
//            System.out.println("Sorry, you lost the battle");
//        System.out.println();
//    }

    public static void printOceanMap(){
        System.out.println();
        //First section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        //Middle section of Ocean Map
        for(int x = 0; x < grid.length; x++) {
            System.out.print(x + "|");

            for (int y = 0; y < grid[x].length; y++){
                System.out.print(grid[x][y]);
            }

            System.out.println("|" + x);
        }

        //Last section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }
}
