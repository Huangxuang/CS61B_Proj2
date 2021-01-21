package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class DrawRandomMap {


    private final static int maxRoomWidth = 5;
    private final static int maxRoomHeight = 5;

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
    DrawRandomMap(int w, int h, int initialX, int initialY, long seed){
        worldWidth = w;
        worldHeight = h;
        initialPosition = new Position(initialX, initialY);
        RANDOM = new Random(seed);
    }

    //Nested class for x,y position.
    private class Position{
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

    //make a room!
    private void makeRoom(Position bottomLeft){
        int botLeftX = bottomLeft.positionX;
        int botLeftY = bottomLeft.positionY;
        //int currentRoomW = RANDOM.nextInt(worldWidth);
        //int currentRoomH = RANDOM.nextInt(worldHeight);
        int topRightX = botLeftX + RANDOM.nextInt(worldWidth);
        int topRightY = botLeftY + RANDOM.nextInt(worldHeight);


    }






    public static void main(String[] args){
        int width = 80;
        int height = 80;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        DrawRandomMap world = new DrawRandomMap(width, height, 1, 1, 45564);
        world.initialize();
    }
}
