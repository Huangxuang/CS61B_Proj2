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

    //Nested class for x,y position.
    private static class Position{
        int bottomLeftX;
        int bottomLeftY;
        //Upper right position;
        int upperRightX;
        int upperRightY;

        Position(int x, int y){
            bottomLeftX = x;
            bottomLeftY = y;
        }

        Position(int x1, int y1, int x2, int y2){
            bottomLeftX = x1;
            bottomLeftY = y1;
            upperRightX = x2;
            upperRightY = y2;
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
        int x = RANDOM.nextInt(worldWidth) - maxRoomWidth; //fix by using checkBoundary method
        int y = RANDOM.nextInt(worldHeight) - maxRoomWidth;

        Position startPoint = new Position(x, y);

        return startPoint;
    }

    //make a room!
    private Position makeRoom(Position initialPoint){

        int botLeftX = initialPoint.bottomLeftX;
        int botLeftY = initialPoint.bottomLeftY;

        //add offset 3 to make a real room.
        int topRightX = botLeftX + RANDOM.nextInt(maxRoomWidth) + 3;
        int topRightY = botLeftY + RANDOM.nextInt(maxRoomHeight) + 3;

        /*for(int x = botLeftX; x <= topRightX; x++) {

            for(int y = botLeftY; y <= topRightY; y++) {
                //this.world[x][y] = Tileset.WALL;
                //distinguish wall and floor, otherwise, draw floor.
                if(x == botLeftX || x == topRightX || y == botLeftY || y == topRightY){
                    this.world[x][y] = Tileset.WALL;
                }
                else {
                    this.world[x][y] = Tileset.FLOOR;
                }
            }
        }*/

        initialPoint.upperRightX = topRightX;
        initialPoint.upperRightY = topRightY;

        makeRoomHelperBottomLeft(initialPoint);

        return initialPoint;

    }
    //Each room has 4 sides, 0 - 3. 0 is on the very left, clockwise.
    //Generate random of halls(1 - 4) on the room.
    /*private void generateHalls(){
        //generate 1 to  4 halls
       // int numberOfHalls = RANDOM.nextInt(4) + 1
        int numberOfHalls = 3;
        //Select which sides to draw
        for (int i = 0; i < numberOfHalls; i++){
            int sideToDraw = RANDOM.nextInt(numberOfHalls);
            //Draw halls according the side number.
            switch (numberOfHalls){
                case 0: westHall();
                case 1: northHall();
                case 2: eastHall();
                case 3: southHall();
            }
        }
    }*/


    private void westHall(Position startPoint){
        // Draw a random hall;
        int hallLength = RANDOM.nextInt(maxHallLength);
        int startX = startPoint.upperRightX;
        int startY = startPoint.upperRightY;
        int endX = startX - hallLength;
        int endY = startY - hallWidth + 1;

        startPoint.bottomLeftX = endX;
        startPoint.bottomLeftY = endY;

        makeRoomHelperTopRight(startPoint);
        /*for(int x = startX; x >= endX; x--) {
            for(int y = startY; y >= endY; y--) {
                //this.world[x][y] = Tileset.WALL;
                //distinguish wall and floor, otherwise, draw floor.
                if(x == startX || x == endX || y == startY || y == endY){
                    this.world[x][y] = Tileset.WALL;
                }
                else {
                    this.world[x][y] = Tileset.FLOOR;
                }
            }
        }*/
    }
    private void northHall(Position startPoint){
        int hallLength = RANDOM.nextInt(maxHallLength);
        int startX = startPoint.bottomLeftX;
        int startY = startPoint.bottomLeftY;
        int endX = startX + hallWidth - 1;
        int endY = startY + hallLength;

        startPoint.upperRightX = endX;
        startPoint.upperRightY = endY;

        makeRoomHelperBottomLeft(startPoint);
    }

    private void eastHall(Position startPoint){
        int hallLength = RANDOM.nextInt(maxHallLength);
        int startX = startPoint.bottomLeftX;
        int startY = startPoint.bottomLeftY;
        int endX = startX + hallLength;
        int endY = startY + hallWidth - 1;

        startPoint.upperRightX = endX;
        startPoint.upperRightY = endY;

        makeRoomHelperBottomLeft(startPoint);
    }

    private void southHall(Position startPoint){
        int hallLength = RANDOM.nextInt(maxHallLength);
        int startX = startPoint.upperRightX;
        int startY = startPoint.upperRightY;

        int endX = startX - hallWidth + 1;
        int endY = startY - hallLength;

        startPoint.bottomLeftX = endX;
        startPoint.bottomLeftY = endY;

        makeRoomHelperTopRight(startPoint);
    }

    //helper method to make room/hallway start at bottomLeft.
    private void makeRoomHelperBottomLeft(Position p){
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
    }

    //helper method to make room/hallway start at topRight
    private void makeRoomHelperTopRight(Position p){
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

    }


    public static void main(String[] args){
        int worldWidth = 20;  //x coordinate
        int worldHeight = 20; //y coordinate

        TERenderer ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

        //TETile[][] world = new TETile[width][height];

        DrawRandomMap game = new DrawRandomMap(worldWidth, worldHeight, 1, 1, 123456);
        Position fixPoint = new Position(10,10, 10, 10 );
        Position fixPoint2 = new Position(0, 0, 10, 10);
        Position startPoint = game.startXY();
        //fill everything with NOTHING
        game.initialize();
        //game.makeRoom(fixPoint);
        //game.westHall(fixPoint);
        //game.northHall(fixPoint2);
        game.southHall(fixPoint2);
        //game.eastHall(fixPoint);
        ter.renderFrame(game.world);

    }
}
