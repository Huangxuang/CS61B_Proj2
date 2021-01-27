/**
 * 01_24: add size record of map and each hall and room,used for largeEnough() method
 * current currentMapSize include overlap arear;
 * */
package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;


public class DrawRandomMap {
    //width and height include 2 side of wall
    private final static int maxRoomWidth = 5;
    private final static int maxRoomHeight = 5;
    private final static int maxHallLength = 8;
    private final static int hallWidth = 3;
    private final static String WEST = "W";
    private final static String NORTH = "N";
    private final static String EAST = "E";
    private final static String SOUTH = "S";
    private final static String ROOM = "R";
    private final static String HALL = "H";
    private final static double percentage = 0.2;
    private static List<Position> allPosition = new ArrayList<>();
    private final static String UPPER = "U";
    private final static String LOWER = "L";
    private final static String LEFT = "LEFT";
    private final static String RIGHT = "RIGHT";


    private Random RANDOM;
    private int worldWidth;
    private int worldHeight;
    private Position initialPosition;
    private TETile[][] world;
    private int currentMapSize = 0;


    /**
     *
     * @param w = width of the world
     * @param h = height of the world
     * @param initialX  initial position of the world
     * @param initialY
     * @param seed = random seed the generate the world
     */
    DrawRandomMap(int w, int h, int initialX, int initialY, long seed) {
        worldWidth = w;
        worldHeight = h;
        initialPosition = new Position(initialX, initialY);
        RANDOM = new Random(seed);
    }

    //return true if currentSize > world size
    private boolean largeEnough() {
        return currentMapSize > percentage * (worldHeight - 1) * (worldHeight -1);
    }

    //Contains bottom left and upper right points coordinates
    private static class Position{

        int bottomLeftX;
        int bottomLeftY;
        int upperRightX;
        int upperRightY;

        Position(int x, int y) {
            bottomLeftX = x;
            bottomLeftY = y;
        }

        Position(int x1, int y1, int x2, int y2) {
            bottomLeftX = x1;
            bottomLeftY = y1;
            upperRightX = x2;
            upperRightY = y2;
        }

        int getDeltaX() {
            return upperRightX - bottomLeftX;
        }

        int getDeltaY() {
            return upperRightY - bottomLeftY;
        }
    }

    //flush the world with Tileset.NOTHING first;
    private void initialize(){
        world = new TETile[worldWidth][worldHeight];
        for(int x = 0; x < worldWidth; x++) {
            for(int y = 0; y < worldHeight; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    //Randomly generate an x,y starting coordinate.
    private Position startXY(){
        int x = RANDOM.nextInt(worldWidth - maxRoomWidth); //fix by using checkBoundary method
        int y = RANDOM.nextInt(worldHeight - maxRoomWidth);

        Position startPoint = new Position(x, y);

        return startPoint;
    }

    //make a room!
    //return the upper right and bottom left points position
    private Position makeRoom(Position initialPoint){
        /*Position initialPoint = new Position(worldWidth / 2,worldHeight / 2,
                worldWidth / 2 + 4,worldHeight / 2 + 4);*/
        int botLeftX = initialPoint.bottomLeftX;
        int botLeftY = initialPoint.bottomLeftY;
        //Calculate upper right point coordinates
        //add offset 3 to make a real room.
        //Actual max width and length is 7
        int topRightX = initialPoint.upperRightX;
        int topRightY = initialPoint.upperRightY;

        makeRoomHelperBottomLeft(initialPoint, ROOM, "k");

        Position roomPositions = new Position(botLeftX,botLeftY,topRightX,topRightY);
        return roomPositions;
    }
    //Each room has 4 sides, 0 - 3. 0 is on the very left, clockwise.
    //Generate random of halls(1 - 4) on the room.
    //Input contains  upper right and bottom left coordinates of the room
    private void generateHalls(Position p) {
        //generate 1 to  4 halls
        //int numberOfHalls = RANDOM.nextInt(4) + 1;
        int numberOfHalls = 1;
        //Select the 1st side to draw hall
        int sideToDraw = RANDOM.nextInt(4);

        for (int i = 0; i < numberOfHalls; i++) {
            //Draw halls according the side number.
            switch (sideToDraw){
                case 0: west(p,HALL);
                break;
                case 1: north(p,HALL);
                break;
                case 2: east(p, HALL);
                break;
                case 3: south(p,HALL);
                break;
            }
            sideToDraw = (sideToDraw + 1) % 4;
        }
    }
    // Draw a westHall from upper right
    //input are the two coordinates of the room to draw halls
    private Position west(Position prePosition, String type){
        int preHeight = prePosition.getDeltaY();
        if (type == HALL){
            int hallLength = RANDOM.nextInt(maxHallLength) + 2;
            int startX = prePosition.bottomLeftX;
            // random select Y  from [room.bottomLeftY + 2, room.upperRightY]
            int startY = RANDOM.nextInt(preHeight - 1) + prePosition.bottomLeftY + 2;

            int endX = startX - hallLength;
            int endY = startY - hallWidth + 1;

            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, WEST, HALL);

            return positionToDraw;
        }else { //for ROOM
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int whereToDraw = RANDOM.nextInt(2);
            //whereToDraw = 0, start at prePosition.bottomLeft corner.
            if(whereToDraw == 0){
                int startX = prePosition.bottomLeftX;
                int startY = prePosition.bottomLeftY + 2;

                int endX = startX - roomWidth;
                int endY = startY - roomHeight;
                Position positionToDraw = new Position(endX, endY, startX, startY);
                //0 = prePosition.bottomLeft.
                makeRoomHelperTopRight(positionToDraw, WEST, LOWER);
                return positionToDraw;
            }
            else{//whereToDraw = 1, start at prePosition.upperLeft corner.
                int startX = prePosition.bottomLeftX;
                int startY = prePosition.upperRightY - 2 + roomHeight;

                int endX = startX - roomWidth;
                int endY = startY - roomHeight;
                Position positionToDraw = new Position(endX, endY, startX, startY);
                //1 = prePosition.bottomLeft.
                makeRoomHelperTopRight(positionToDraw, WEST, UPPER);
                return positionToDraw;
            }
        }

    }
    private Position north(Position prePosition, String type){
        int preWidth = prePosition.getDeltaX();
        if(type == HALL) {
            int hallLength = RANDOM.nextInt(maxHallLength) + 2;

            //int startX = roomPositions.bottomLeftX;
            //range : [bottomLeftX, room.UpperRightX -2]
            int startX = RANDOM.nextInt(preWidth - 1) + prePosition.bottomLeftX;
            int startY = prePosition.upperRightY;

            int endX = startX + hallWidth - 1;
            int endY = startY + hallLength;

            Position positionToDraw = new Position(startX, startY, endX, endY);

            makeRoomHelperBottomLeft(positionToDraw, NORTH, HALL);
            return positionToDraw;
        } else {
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int whereToDraw = RANDOM.nextInt(2);
            //whereToDraw = 0, start at prePosition.upperLeft corner.
            if(whereToDraw == 0){
                int startX = prePosition.bottomLeftX + 2 - roomWidth;
                int startY = prePosition.upperRightY;

                int endX = startX + roomWidth;
                int endY = startY + roomHeight;
                Position positionToDraw = new Position(startX, startY, endX, endY);
                //0 = prePosition.bottomLeft.

                makeRoomHelperBottomLeft(positionToDraw, NORTH, LEFT);
                return positionToDraw;
            }
            else{//whereToDraw = 1, start at prePosition.upperLeft corner.
                int startX = prePosition.upperRightX - 2;
                int startY = prePosition.upperRightY;

                int endX = startX + roomWidth;
                int endY = startY + roomHeight;
                Position positionToDraw = new Position(startX, startY, endX, endY);
                //0 = prePosition.bottomLeft.

                makeRoomHelperBottomLeft(positionToDraw, NORTH, RIGHT);
                return positionToDraw;
            }
        }

    }

    //draw hall or room directing east, based on type.
    private Position east(Position prePosition, String type){
        int preHeight = prePosition.getDeltaY();
        if (type == HALL) {
            int hallLength = RANDOM.nextInt(maxHallLength) + 2;

            int startX = prePosition.upperRightX;
            int startY = prePosition.bottomLeftY + RANDOM.nextInt(preHeight - 1);

            int endX = startX + hallLength;
            int endY = startY + hallWidth - 1;

            Position positionToDraw = new Position(startX, startY, endX, endY);
            makeRoomHelperBottomLeft(positionToDraw, EAST, HALL);
            //makeRoomHelperBottomLeft(startPoint);
            return positionToDraw;
        }else {
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int whereToDraw = RANDOM.nextInt(2);
            //whereToDraw = 0, start at prePosition.bottomRight corner.
            if(whereToDraw == 0){
                int startX = prePosition.upperRightX;
                int startY = prePosition.bottomLeftY + 2 - roomHeight;

                int endX = startX + roomWidth;
                int endY = startY + roomHeight;
                Position positionToDraw = new Position(startX, startY, endX, endY);
                //0 = prePosition.bottomLeft.

                makeRoomHelperBottomLeft(positionToDraw, EAST, LOWER);
                return positionToDraw;
            }
            else{//whereToDraw = 1, start at prePosition.upperRight corner
                int startX = prePosition.upperRightX;
                int startY = prePosition.upperRightY - 2;

                int endX = startX + roomWidth;
                int endY = startY + roomHeight;
                Position positionToDraw = new Position(startX, startY, endX, endY);
                //0 = prePosition.bottomLeft.

                makeRoomHelperBottomLeft(positionToDraw, EAST, UPPER);
                return positionToDraw;
            }
        }

    }

    //draw hall or room directing south, based on type.
    private Position south(Position prePosition, String type){
        int preWidth = prePosition.getDeltaX();
        //Position positionToDraw = new Position(0, 0, 0, 0)
        if (type == HALL ){
            int hallLength = RANDOM.nextInt(maxHallLength) + 2;
            int startX = prePosition.bottomLeftX + 2 + RANDOM.nextInt(preWidth - 1);
            int startY = prePosition.bottomLeftY;

            int endX = startX - hallWidth + 1;
            int endY = startY - hallLength;

            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, SOUTH, HALL);
            return positionToDraw;
        }
        else{
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int whereToDraw = RANDOM.nextInt(2);
            //whereToDraw = 0, start at prePosition.bottomLeft corner.
            if(whereToDraw == 0){
                int startX = prePosition.bottomLeftX + 2;
                int startY = prePosition.bottomLeftY;

                int endX = startX - roomWidth;
                int endY = startY - roomHeight;
                Position positionToDraw = new Position(endX, endY, startX, startY);
                //0 = prePosition.bottomLeft.
                makeRoomHelperTopRight(positionToDraw, SOUTH, LEFT);
                return positionToDraw;
            }
            else{//whereToDraw = 1, start at prePosition.upperLeft corner.
                int startX = prePosition.upperRightX - 2 + roomWidth;
                int startY = prePosition.bottomLeftY;

                int endX = startX - roomWidth;
                int endY = startY - roomHeight;
                Position positionToDraw = new Position(endX, endY, startX, startY);
                //1 = prePosition.bottomLeft.
                makeRoomHelperTopRight(positionToDraw, SOUTH, RIGHT);
                return positionToDraw;
            }
        }
        //return positionToDraw;

    }

    //helper method to draw room/hallway start at bottomLeft.
    //The input Position P contains the botleft and upper right points information
    //Add variable size to record the size of current Room and Hall
    private void makeRoomHelperBottomLeft(Position p, String type, String whereToDraw){
        //check if overlap with previous shape or out of world boundary
        if (checkOverlap(p) || checkBoundary(p)) {
            System.out.println("Hit boundary");
            return;
        }
        int x1 = p.bottomLeftX;
        int y1 = p.bottomLeftY;
        int x2 = p.upperRightX;
        int y2 = p.upperRightY;
        int size = 0;
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
                size += 1;
                //distinguish wall and floor, otherwise, draw floor.
                if(x == x1 || x == x2 || y == y1 || y == y2){
                    this.world[x][y] = Tileset.WALL;
                }
                else {
                    this.world[x][y] = Tileset.FLOOR;
                }
            }
        }
        if(type == NORTH && whereToDraw == HALL) {
            this.world[x1+1][y1] = Tileset.FLOOR;
        }
        if(type == NORTH && whereToDraw == LEFT){
            this.world[x2-1][y1] = Tileset.FLOOR;
        }
        if(type == NORTH && whereToDraw == RIGHT){
            this.world[x1+1][y1] = Tileset.FLOOR;
        }
        if(type == EAST && whereToDraw == HALL) {
            this.world[x1][y1+1] = Tileset.FLOOR;
        }
        if(type == EAST && whereToDraw == UPPER){
            this.world[x1][y1+1] = Tileset.FLOOR;
        }
        if(type == EAST && whereToDraw == LOWER){
            this.world[x1][y2-1] = Tileset.FLOOR;
        }

        //update current size of map
        currentMapSize += size;
        //store shape positions to allPosition
        allPosition.add(p);
    }

    //helper method to draw room/hallway start at topRight
    private void makeRoomHelperTopRight(Position p, String type, String whereToDraw){
        //check if overlap with previous shape or out of world boundary
        if (checkOverlap(p) || checkBoundary(p)) {
           System.out.println("Hit boundary");
            return;
        }
        int x1 = p.upperRightX;
        int y1 = p.upperRightY;
        int x2 = p.bottomLeftX;
        int y2 = p.bottomLeftY;
        int size = 0;
        for(int x = x1; x >= x2; x--) {
            for(int y = y1; y >= y2; y--) {
                size += 1;
                //distinguish wall and floor, otherwise, draw floor.
                if(x == x1 || x == x2 || y == y1 || y == y2){
                    this.world[x][y] = Tileset.WALL;
                }
                else {
                    this.world[x][y] = Tileset.FLOOR;
                }
            }
        }
        if(type == WEST && whereToDraw == HALL){
            this.world[x1][y1-1] = Tileset.FLOOR;
        }
        if(type == SOUTH && whereToDraw == HALL){
            this.world[x1-1][y1] = Tileset.FLOOR;
        }
        if(type == SOUTH && whereToDraw == LEFT){
            this.world[x1-1][y1] = Tileset.FLOOR;
        }
        if(type == SOUTH && whereToDraw == RIGHT){
            this.world[x2+1][y1] = Tileset.FLOOR;
        }
        if(type == WEST && whereToDraw == UPPER) {
            this.world[x1][y2 + 1] = Tileset.FLOOR;
        }
        if(type == WEST && whereToDraw == LOWER) {
            this.world[x1][y1 - 1] = Tileset.FLOOR;
        }

        //update current size of map
        currentMapSize += size;
        //store shape positions to allPosition
        allPosition.add(p);
    }
    //generate the whole world
    public void generateWorld(Position initialPosition){
        makeRoom(initialPosition);
        int size;
        //int size = allPosition.size();
        int i = 0;
        for(; i < allPosition.size(); i++){
            //check if current map is large enough
            if (largeEnough()){
                System.out.println("Reaches 60% of capacity!");
                return;
            }
                Position p = allPosition.get(i);
                nextStuff(p);

        }
    }

    //draw next stuff connected to a Hall
    public void nextStuff (Position prePosition){
        //generate 1 to  3 halls/rooms
        int numberOfItems = 4;
        //Select the 1st side to draw hall
        int sideToStart = RANDOM.nextInt(4);
        //RANDOM.nextInt(4);
        //switch case, room or hall.
       for(int i = 0; i < numberOfItems; i++) {
           //randomly selects the next side will be hall or room.
           int roomOrHall = RANDOM.nextInt(2);
           //int roomOrHall = 1;
           switch (roomOrHall) {
               case 0:
                   switch(sideToStart){
                       case 0: west(prePosition, ROOM); break;
                       case 1: north(prePosition, ROOM); break;
                       case 2: east(prePosition, ROOM); break;
                       case 3: south(prePosition, ROOM); break;
                   }
                   break;
               case 1: switch(sideToStart){
                       case 0: west(prePosition, HALL); break;
                       case 1: north(prePosition, HALL); break;
                       case 2: east(prePosition, HALL); break;
                       case 3: south(prePosition, HALL); break;
                   }
                   break;
           }
           sideToStart = (sideToStart + 1) % 4;
       }
    }
    //check if nextPosition is out of world boundary
    private boolean checkBoundary(Position nextPosition) {
        int x2 = nextPosition.upperRightX;
        int y2 = nextPosition.upperRightY;
        int x1 = nextPosition.bottomLeftX;
        int y1 = nextPosition.bottomLeftY;
        if ((x1 < 0 || x1 > worldWidth - 1 || x2 < 0 || x2 > worldWidth - 1) ||
                (y1 < 0 || y1 > worldHeight -1 ||y2 < 0 || y2 > worldHeight - 1)) {
            return true;
        }
        return false;
    }
    //check if next shape at nextPosition is overlapped with any previous shape
    private boolean checkOverlap(Position nextPosition){
        //go through allPosition list, check if overlap with all previous position
        //return true if overlap
        //(x1,y1) is bot left point, (x2,y2)is upper right corner
        int x2 = nextPosition.upperRightX;
        int y2 = nextPosition.upperRightY;
        int x1 = nextPosition.bottomLeftX;
        int y1 = nextPosition.bottomLeftY;
        for (Position p : allPosition){
            //check if the upper left corner of nextPosition is inside any previous hall/room
            if  (( x1< (p.upperRightX) && x1> (p.bottomLeftX) ) && (y2 < (p.upperRightY) && (y2  > p.bottomLeftY))){
                return true;
            }
            //check if the bottom left corner of nextPosition is inside any previous hall/room
            if  (( x1 < (p.upperRightX) && x1 > (p.bottomLeftX)) && (y1< (p.upperRightY)  && y1> (p.bottomLeftY))){
                return true;
            }
            //check if the bottom right corner of nextPosition is inside any previous hall/room
            if  (( x2 < (p.upperRightX) && x2 > (p.bottomLeftX)) && (y1 < (p.upperRightY) && y1 > (p.bottomLeftY))){
                return true;
            }
            //check if the upper right corner of nextPosition is inside any previous hall/room
            if  (( x2 < (p.upperRightX) && x2 > (p.bottomLeftX)) && (y2< (p.upperRightY) && y2 > (p.bottomLeftY ))){
                return true;
            }
            //check if left side of nextPosition is overlap any previous hall/room
            if ( (x1 <= p.upperRightX && x1 >= p.bottomLeftX) && (y2 >= p.upperRightY && y1 <= p.bottomLeftY)){
                return true;
            }
            //check if right side of nextPosition is overlap with any previous hall/room
            if ( (x2 <= p.upperRightX && x2 >= p.bottomLeftX) && (y2 >= p.upperRightY && y1 <= p.bottomLeftY)){
                return true;
            }
            //check if top side of nextPosition is overlapping with any previous hall/rooms
            if ( (x2 >= p.upperRightX && x1 <= p.bottomLeftX) && (y2 <= p.upperRightY && y2 >= p.bottomLeftY)){
                return true;
            }
            //check if bottom side of nextPosition is overlapping with any previous hall/rooms
            if ( (x2 >= p.upperRightX && x1 <= p.bottomLeftX) && (y1 <= p.upperRightY && y1 >= p.bottomLeftY)){
                return true;
            }
            //check if x1,x2 is inside any previous hall/room
            if ( (x2 <= p.upperRightX && x1 >= p.bottomLeftX) && (y2 >= p.upperRightY && y1 <= p.bottomLeftY)){
                return true;
            }
            if ( (y2 <= p.upperRightY && y1 >= p.bottomLeftY) && (x2 >= p.upperRightX && x1 <= p.bottomLeftX)){
                return true;
            }

        }

        return false;
    }


    public static void main(String[] args){
        int worldWidth = 80;  //x coordinate
        int worldHeight = 50; //y coordinate

        TERenderer ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

        //TETile[][] world = new TETile[width][height];

        DrawRandomMap game = new DrawRandomMap(worldWidth, worldHeight, 1, 1, 788);
        Position fixPoint = new Position(40,45, 45, 49 );
        Position fixPoint2 = new Position(10,10, 50, 50);
        Position startPoint = game.startXY();
        //fill everything with NOTHING
        game.initialize();
        Position roomPosition = game.makeRoom(fixPoint);
        //game.makeRoom(fixPoint);
        game.generateWorld(fixPoint);
        //game.nextStuff(roomPosition);
        /*Position newPosition = game.south(roomPosition, ROOM);
        Position newPosition2 = game.south(newPosition, HALL);
        Position newPosition3 = game.south(newPosition2, ROOM);
        Position newPosition4 = game.south(newPosition3, ROOM);
        Position newPosition5 = game.east(newPosition4, ROOM);
        Position newPosition6 = game.north(newPosition4, ROOM);
        Position newPosition7 = game.north(newPosition4, HALL);*/

        //Position newPosition2 = game.south(newPosition, ROOM);*/
       // Position newPosition3 = game.south(newPosition2, ROOM);
        /*Position newPosition4 = game.west(newPosition3, ROOM);
        Position newPosition5 = game.west(newPosition4, HALL);
        Position newPosition6 = game.north(newPosition5, ROOM);
        Position newPosition7 = game.north(newPosition6, ROOM);
        Position newPosition8 = game.west(newPosition7, HALL);*/

        //game.southHall(roomPosition);
        //game.northHall(fixPoint2);
        //game.southHall(fixPoint2);
        //game.eastHall(fixPoint);
        ter.renderFrame(game.world);
        System.out.println(game.currentMapSize);
    }
}

