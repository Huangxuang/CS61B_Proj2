package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
//import java.util.List;


public class DrawRandomMap {

    private final static int maxRoomWidth = 5;
    private final static int maxRoomHeight = 5;
    private final static int maxHallLength = 10;
    private final static int hallWidth = 3;
    private final static String WEST = "W";
    private final static String NORTH = "N";
    private final static String EAST = "E";
    private final static String SOUTH = "S";
    private final static String ROOM = "R";


    private Random RANDOM;
    private int worldWidth;
    private int worldHeight;
    private Position initialPosition;
    private TETile[][] world;


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
        int topRightX = botLeftX + RANDOM.nextInt(maxRoomWidth) + 3;
        int topRightY = botLeftY + RANDOM.nextInt(maxRoomHeight) + 3;

        initialPoint.upperRightX = topRightX;
        initialPoint.upperRightY = topRightY;

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
        int numberOfHalls = 4;
        //Select the 1st side to draw hall
        int sideToDraw = 2;
        //RANDOM.nextInt(4);

        for (int i = 0; i < numberOfHalls; i++) {
            //Draw halls according the side number.
            switch (sideToDraw){
                case 0:
                    westHall(p);
                    break;
                case 1:
                    northHall(p);
                    break;
                case 2:
                    eastHall(p);
                    break;
                case 3:
                    southHall(p);
                    break;
            }
            sideToDraw = (sideToDraw + 1) % 4;
        }
    }
    // Draw a westHall from upper right
    //input are the two coordinates of the room to draw halls
    private Position westHall(Position roomPositions){
        int roomHeight = roomPositions.getDeltaY();
        int hallLength = RANDOM.nextInt(maxHallLength);

        int startX = roomPositions.bottomLeftX;
        // random select Y  from [room.bottomLeftY + 2, room.upperRightY]
        int startY = RANDOM.nextInt(roomHeight - 1) + roomPositions.bottomLeftY + 2;

        int endX = startX - hallLength;
        int endY = startY - hallWidth + 1;
        //roomPositions.bottomLeftX = endX;
        //roomPositions.bottomLeftY = endY;
        Position positionToDraw = new Position(endX, endY, startX, startY);
        makeRoomHelperTopRight(positionToDraw, WEST);

        return positionToDraw;

    }
    private Position northHall(Position roomPositions){
        int roomWidth = roomPositions.getDeltaX();
        int hallLength = RANDOM.nextInt(maxHallLength);

        //int startX = roomPositions.bottomLeftX;
        //range : [bottomLeftX, room.UpperRightX -2]
        int startX = RANDOM.nextInt(roomWidth - 1) + roomPositions.bottomLeftX;
        //int startX = RANDOM.nextInt(roomWidth - 1) + roomPositions.upperRightX - 2;
        int startY = roomPositions.upperRightY;

        int endX = startX + hallWidth - 1;
        int endY = startY + hallLength;

        Position positionToDraw = new Position(startX, startY, endX, endY);
       // roomPositions.upperRightX = endX;
       // roomPositions.upperRightY = endY;
        makeRoomHelperBottomLeft(positionToDraw, NORTH);
        return positionToDraw;
       // makeRoomHelperBottomLeft(startPoint);
    }

    private Position eastHall(Position roomPositions){
        int roomHeight = roomPositions.getDeltaY();
        int hallLength = RANDOM.nextInt(maxHallLength);

        int startX = roomPositions.upperRightX;
        int startY = roomPositions.bottomLeftY + RANDOM.nextInt(roomHeight - 1);

        int endX = startX + hallLength;
        int endY = startY + hallWidth - 1;

        //roomPositions.upperRightX = endX;
        //roomPositions.upperRightY = endY;

        Position positionToDraw = new Position(startX, startY, endX, endY);
        makeRoomHelperBottomLeft(positionToDraw, EAST);
        //makeRoomHelperBottomLeft(startPoint);
        return positionToDraw;
    }

    private Position southHall(Position roomPositions){
        int roomWidth = roomPositions.getDeltaX();
        int hallLength = RANDOM.nextInt(maxHallLength);

        int startX = roomPositions.bottomLeftX + 2 +RANDOM.nextInt(roomWidth - 1);
        int startY = roomPositions.bottomLeftY;

        int endX = startX - hallWidth + 1;
        int endY = startY - hallLength;

        Position positionToDraw = new Position(endX, endY, startX, startY);
        makeRoomHelperTopRight(positionToDraw, SOUTH);
        return positionToDraw;
    }

    //helper method to make room/hallway start at bottomLeft.
    //The input Position P contains the botleft and upper right points information
    private void makeRoomHelperBottomLeft(Position p, String type){
        int x1 = p.bottomLeftX;
        int y1 = p.bottomLeftY;
        int x2 = p.upperRightX;
        int y2 = p.upperRightY;

        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
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
    }

    //helper method to make room/hallway start at topRight
    private void makeRoomHelperTopRight(Position p, String type){
        int x1 = p.upperRightX;
        int y1 = p.upperRightY;
        int x2 = p.bottomLeftX;
        int y2 = p.bottomLeftY;
        for(int x = x1; x >= x2; x--) {
            for(int y = y1; y >= y2; y--) {
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

    }


    public static void main(String[] args){
        int worldWidth = 30;  //x coordinate
        int worldHeight = 30; //y coordinate

        TERenderer ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

        //TETile[][] world = new TETile[width][height];

        DrawRandomMap game = new DrawRandomMap(worldWidth, worldHeight, 1, 1, 1658);
        Position fixPoint = new Position(5,10, 5, 5 );
        Position fixPoint2 = new Position(0, 0, 10, 10);
        Position startPoint = game.startXY();
        //fill everything with NOTHING
        game.initialize();
        Position roomPosition = game.makeRoom(fixPoint);
        game.generateHalls(roomPosition);
        //game.southHall(roomPosition);
        //game.northHall(fixPoint2);
        //game.southHall(fixPoint2);
        //game.eastHall(fixPoint);
        ter.renderFrame(game.world);
    }
}

