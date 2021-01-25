/**
 * 01_24: add size record of map and each hall and room,used for largeEnough() method
 * current currentMapSize include overlap arear;
 * */
package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Random;
//import java.util.List;


public class DrawRandomMap {
    //width and height include 2 side of wall
    private final static int maxRoomWidth = 5;
    private final static int maxRoomHeight = 5;
    private final static int maxHallLength = 10;
    private final static int hallWidth = 3;
    private final static String WEST = "W";
    private final static String NORTH = "N";
    private final static String EAST = "E";
    private final static String SOUTH = "S";
    private final static String ROOM = "R";
    private final static String HALL = "H";
    private final static double percentage = 0.8;


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

        int botLeftX = initialPoint.bottomLeftX;
        int botLeftY = initialPoint.bottomLeftY;
        //Calculate upper right point coordinates
        //add offset 3 to make a real room.
        //Actual max width and length is 7
        int topRightX = initialPoint.upperRightX;
        int topRightY = initialPoint.upperRightY;
        //int topRightX = botLeftX + RANDOM.nextInt(maxRoomWidth) + 3;
        //int topRightY = botLeftY + RANDOM.nextInt(maxRoomHeight) + 3;

       //initialPoint.upperRightX = topRightX;
        //initialPoint.upperRightY = topRightY;

        makeRoomHelperBottomLeft(initialPoint, ROOM);

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
            int hallLength = RANDOM.nextInt(maxHallLength);

            int startX = prePosition.bottomLeftX;
            // random select Y  from [room.bottomLeftY + 2, room.upperRightY]
            int startY = RANDOM.nextInt(preHeight - 1) + prePosition.bottomLeftY + 2;

            int endX = startX - hallLength;
            int endY = startY - hallWidth + 1;

            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, WEST);

            return positionToDraw;
        }else {
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int startX = prePosition.bottomLeftX;
            // random select Y  from [room.bottomLeftY + 2, room.upperRightY]
            int startY = RANDOM.nextInt(roomHeight - 2) + prePosition.bottomLeftY + 2;

            int endX = startX - roomHeight;
            int endY = startY - roomWidth + 1;
            //roomPositions.bottomLeftX = endX;
            //roomPositions.bottomLeftY = endY;
            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, WEST);

            return positionToDraw;
        }

    }
    private Position north(Position prePosition, String type){
        int preWidth = prePosition.getDeltaX();
        if(type == HALL) {
            int hallLength = RANDOM.nextInt(maxHallLength);

            //int startX = roomPositions.bottomLeftX;
            //range : [bottomLeftX, room.UpperRightX -2]
            int startX = RANDOM.nextInt(preWidth - 1) + prePosition.bottomLeftX;
            int startY = prePosition.upperRightY;

            int endX = startX + hallWidth - 1;
            int endY = startY + hallLength;

            Position positionToDraw = new Position(startX, startY, endX, endY);

            makeRoomHelperBottomLeft(positionToDraw, NORTH);
            return positionToDraw;
        } else {
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int startX = RANDOM.nextInt(preWidth - 2) + prePosition.bottomLeftX;
            int startY = prePosition.upperRightY;

            int endX = startX + roomWidth - 1;
            int endY = startY + roomHeight;

            Position positionToDraw = new Position(startX, startY, endX, endY);

            makeRoomHelperBottomLeft(positionToDraw, NORTH);
            return positionToDraw;
        }

    }

    //draw hall or room directing east, based on type.
    private Position east(Position prePosition, String type){
        int preHeight = prePosition.getDeltaY();
        if (type == HALL) {
            int hallLength = RANDOM.nextInt(maxHallLength);

            int startX = prePosition.upperRightX;
            int startY = prePosition.bottomLeftY + RANDOM.nextInt(preHeight - 1);

            int endX = startX + hallLength;
            int endY = startY + hallWidth - 1;

            Position positionToDraw = new Position(startX, startY, endX, endY);
            makeRoomHelperBottomLeft(positionToDraw, EAST);
            //makeRoomHelperBottomLeft(startPoint);
            return positionToDraw;
        }else {
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;

            int startX = prePosition.upperRightX;
            int startY = prePosition.bottomLeftY + RANDOM.nextInt(preHeight - 2);

            int endX = startX + roomWidth;
            int endY = startY + roomHeight - 1;

            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, SOUTH);
            return positionToDraw;
        }


    }

    //draw hall or room directing south, based on type.
    private Position south(Position prePosition, String type){
        int preWidth = prePosition.getDeltaX();
        //Position positionToDraw = new Position(0, 0, 0, 0)
        if (type == HALL ){
            int hallLength = RANDOM.nextInt(maxHallLength);
            int startX = prePosition.bottomLeftX + 2 + RANDOM.nextInt(preWidth - 1);
            int startY = prePosition.bottomLeftY;

            int endX = startX - hallWidth + 1;
            int endY = startY - hallLength;

            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, SOUTH);
            return positionToDraw;
        }
        else{
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 4;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 4;
            int startX = prePosition.bottomLeftX + 1 + RANDOM.nextInt(preWidth - 1);
            int startY = prePosition.bottomLeftY;

            int endX = startX - roomWidth + 1;
            int endY = startY - roomHeight;

            Position positionToDraw = new Position(endX, endY, startX, startY);
            makeRoomHelperTopRight(positionToDraw, SOUTH);
            return positionToDraw;
        }
        //return positionToDraw;

    }

    //helper method to draw room/hallway start at bottomLeft.
    //The input Position P contains the botleft and upper right points information
    //Add variable size to record the size of current Room and Hall
    private void makeRoomHelperBottomLeft(Position p, String type){
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
        if(type == NORTH) {
            this.world[x1+1][y1] = Tileset.FLOOR;
        }
        if(type == EAST) {
            this.world[x1][y1+1] = Tileset.FLOOR;
        }
        //update current size of map
        currentMapSize += size;
    }

    //helper method to draw room/hallway start at topRight
    private void makeRoomHelperTopRight(Position p, String type){
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
        if(type == WEST){
            this.world[x1][y1-1] = Tileset.FLOOR;
        }
        if(type == SOUTH){
            this.world[x1-1][y1] = Tileset.FLOOR;
        }
        //update current size of map
        currentMapSize += size;
    }

    /*
    //randomly generate room size
    private int[] generateRoomSize(String type){
        if(type == HALL ) {
            int roomWidth = RANDOM.nextInt(maxRoomWidth) + 3;
            int roomHeight = RANDOM.nextInt(maxRoomHeight) + 3;
            int[] size = new int[]{roomWidth, roomHeight};
            return size;
        }
        else return null;
    }
     */

    //draw next stuff connected to a Hall
    /*public void nextStuff (Position prePosition){
        //generate 1 to  3 halls/rooms
        int numberOfSides = RANDOM.nextInt(3) + 1;
        //Select the 1st side to draw hall
        int sideToDraw = RANDOM.nextInt(4);

        //RANDOM.nextInt(4);
        //switch case, room or hall.
       for(int i = 0; i < numberOfSides; i++) {
           //randomly selects the next side will be hall or room.
           int roomOrHall = RANDOM.nextInt(2);
           switch (roomOrHall) {
               case 0:
                   //update new random prePosition entering makeRoom.
                   int[] widthHeight = generateRoomSize();

                   makeRoom(prePosition);
                   break;
               case 1:
                   //generateHalls(p);
                   break;
           }
           sideToDraw = (sideToDraw + 1) % 4;
       }
    }*/



    public static void main(String[] args){
        int worldWidth = 30;  //x coordinate
        int worldHeight = 30; //y coordinate

        TERenderer ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

        //TETile[][] world = new TETile[width][height];

        DrawRandomMap game = new DrawRandomMap(worldWidth, worldHeight, 1, 1, 16656);
        Position fixPoint = new Position(15,15, 19, 19 );
        Position fixPoint2 = new Position(0, 0, 10, 10);
        Position startPoint = game.startXY();
        //fill everything with NOTHING
        game.initialize();
        Position roomPosition = game.makeRoom(fixPoint);

        game.makeRoom(roomPosition);
        Position newPosition = game.south(roomPosition, HALL);
        game.west(newPosition, ROOM);

        //game.southHall(roomPosition);
        //game.northHall(fixPoint2);
        //game.southHall(fixPoint2);
        //game.eastHall(fixPoint);
        ter.renderFrame(game.world);
        System.out.println(game.currentMapSize);
    }
}

