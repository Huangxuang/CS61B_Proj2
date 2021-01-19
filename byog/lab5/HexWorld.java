package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;

    // put a flower at position x,y.
    public static void putAFlower(int x, int y){

    }
    //  The helper method for addHexagon to find the start point of ith line
    //  for a Hexagon of size s, starting at (x,y)
    private static int findX(int s, int i, int x){
        int ithX = 0;
        if (i < s ) {
            ithX = x - i;
        } else {
            ithX = x - (2 * s - i - 1);
        }
        return ithX;
    }

    // The helper method for addHexagon to print 'a' for current line
    private  static void printCharacter(TETile[][] world, int s, int i, int x, int y){
        int totalCharacter;
        if (i < s){
              totalCharacter = s + 2 * i;
        } else {
        totalCharacter = s + 2* (2 * s - i - 1);
        }
        for (int k = 0; k < totalCharacter; k++){
            world[x+k][y] = Tileset.FLOWER;
        }
    }

    //draw a Hexagon  of side length s at position (x,y) in the world
    public static void addHexagon(TETile[][] world, int s, int x, int y){
        int currentX;
        int currentY;
        int totalLines = 2*s;
        for (int i = 0; i < totalLines; i++){
            currentX = findX(s,i,x);
            currentY = y - i;
           printCharacter(world,s,i,currentX,currentY);
        }

    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        // initialize tiles
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        addHexagon(world,6,30,30);
        ter.renderFrame(world);
    }

}
