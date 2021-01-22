package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class DrawRandomMap {


    private final static int maxRoomWidth = 5;
    private final static int maxRoomHeight = 5;

    private final Random RANDOM;
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
    DrawRandomMap(int w, int h, int initialX, int initialY, long seed){
        worldWidth = w;
        worldHeight = h;
        initialPosition = new Position(initialX, initialY);
        RANDOM = new Random(seed);
    }

    //Nested class for x,y position.
    private static class Position{
        int positionX;
        int positionY;

        Position(int x, int y){
            positionX = x;
            positionY = y;
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
    private void makeRoom(Position bottomLeft){

        int botLeftX = bottomLeft.positionX;
        int botLeftY = bottomLeft.positionY;

        //add offset 3 to make a real room.
        int topRightX = botLeftX + RANDOM.nextInt(maxRoomWidth) + 3;
        int topRightY = botLeftY + RANDOM.nextInt(maxRoomHeight) + 3;

        for(int x = botLeftX; x <= topRightX; x++) {

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
        }

    }











    public static void main(String[] args){
        int worldWidth = 20;  //x coordinate
        int worldHeight = 20; //y coordinate

        TERenderer ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

        //TETile[][] world = new TETile[width][height];

        DrawRandomMap game = new DrawRandomMap(worldWidth, worldHeight, 1, 1, 55);

        Position startPoint = game.startXY();
        //fill everything with NOTHING
        game.initialize();
        game.makeRoom(startPoint);
        ter.renderFrame(game.world);

        /*TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];
        // initialize tiles
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.FLOWER;
            }
        }*/
        //ter.renderFrame(world.world);
    }
}
