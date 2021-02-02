package proj2.byog.Core;

import proj2.byog.TileEngine.TERenderer;
import proj2.byog.TileEngine.TETile;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.introcs.StdDraw;
import proj2.byog.TileEngine.Tileset;

import java.awt.*;
import java.io.*;
import java.util.Locale;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    public TETile[][] world;

    public  DrawRandomMap map;
    private long seed;

    private boolean setupMode = true;
    private static final String NORTH = "w";
    private static final String SOUTH = "s";
    private static final String WEST = "a";
    private static final String EAST = "d";
    public static int playerX,playerY ;



    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */ void playWithKeyboard() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear();
        StdDraw.clear(Color.black);
        //Draw main menu UI
        drawFrame("", "startUI");

    }

    public void drawFrame(String s,String type) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.clear();
        StdDraw.clear(Color.black);

        if (type == "startUI" ){
            Font startFont = new Font("Monaco", Font.BOLD, 60);
            StdDraw.setFont(startFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, HEIGHT - 4, "CS61B: The Tile Game" );

            Font menuFont = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(menuFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, midHeight + 3, "New Game(N)" );
            StdDraw.text(midWidth, midHeight, "Load Game(L)" );
            StdDraw.text(midWidth, midHeight - 3, "Quit(Q)" );
        }

        else if (type == "askSeed" ){
            Font bigFont = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(bigFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, midHeight, "Please type in your Seed");
        }

        else {
            Font bigFont = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(bigFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, midHeight, s);
        }
            StdDraw.show();

    }

    //read return next keyboard input
    public String keyboardInput(){
        String input ="";
        while(input.length() < 1){
            if(!StdDraw.hasNextKeyTyped()){
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input = Character.toString(key);

            input = input.toLowerCase();
            System.out.println(input);

        }
        return input;
    }

    //processInput(), distinguish keyboard input between menu and movement.
    //i.e, New Game, Load and AWSD.
    public String processInput(String input) throws IOException{
       // if(setupMode) {
            switch (input) {
                case "n":
                    setupMode = false;
                    newGame();
                    break;
                //System.out.println("Input N");break;
                case "l": load();
                    System.out.println("Input L");
                    setupMode = false;
                    break;
                case "q":
                    saveAndQuit();
                    System.out.println("Input Q");
                    break;
                default:
                    System.out.println("Invalid input, please re-enter!");
                    //keyboardInput();
                    processInput(keyboardInput());
                case NORTH: move(NORTH);
                    break;
                case WEST: move(WEST);
                    break;
                case SOUTH: move(SOUTH);
                    break;
                case EAST: move(EAST);
                    break;
            }
        //ter.renderFrame(world);
        //}
       // else{
        //    move(input);
        //}
        return input;
    }
    public void move(String type){
         //move to North
        StdDraw.enableDoubleBuffering();
        switch (type){
            case NORTH:
                if(world[playerX][playerY + 1].equals(Tileset.FLOOR)){
                    world[playerX][playerY + 1] = Tileset.PLAYER;
                    world[playerX][playerY ] = Tileset.FLOOR;
                    playerY += 1;
                    ter.renderFrame(world);
                }
                break;
            case SOUTH:
                if(world[playerX][playerY - 1].equals(Tileset.FLOOR)){
                    world[playerX][playerY - 1] = Tileset.PLAYER;
                    world[playerX][playerY ] = Tileset.FLOOR;
                    playerY -= 1;
                    ter.renderFrame(world);
                }
                break;
            case WEST:
                if(world[playerX - 1][playerY].equals(Tileset.FLOOR)){
                    world[playerX - 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY ] = Tileset.FLOOR;
                    playerX -= 1;
                    ter.renderFrame(world);
                }
                break;
            case EAST:
                if(world[playerX + 1][playerY].equals(Tileset.FLOOR)){
                    world[playerX + 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY ] = Tileset.FLOOR;
                    playerX += 1;
                    ter.renderFrame(world);
                }
                break;

        }
    }

    // Ask for seed and generate a new world based on this seed
    public DrawRandomMap newGame() throws IOException {
        drawFrame("","askSeed");
        String input =" ";
        //Take in seed
            while (input.charAt(input.length() - 1) != 's') {
                if (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                char key = StdDraw.nextKeyTyped();
                input += Character.toString(key);
                input = input.toLowerCase();
                drawFrame(input, "");
            }
            //System.out.println(input);
            String seedToUse = input.substring(1,input.length() - 1);
            seed = Long.parseLong(seedToUse);
            //System.out.println(seed);
        this.map = new DrawRandomMap(80,40,seed);
        world = map.generateWorld();
        System.out.println(TETile.toString(world));
        //Draw a player at Location (worldWidth / 2 + 1,worldHeight / 2 +1)
        world[WIDTH / 2 + 1][HEIGHT / 2 +1] = Tileset.PLAYER;
        playerX = WIDTH / 2 + 1;
        playerY = HEIGHT/2 + 1;
        ter.renderFrame(world);
        //try quitAndSave method, checks for q input.
        //processInput(keyboardInput());
        processInputRecursively();
        return map;

    }

    //take in user input and move accordingly, until received a q for quiteAndSave;
    private void processInputRecursively() throws IOException{
        while(true){
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            String movement = Character.toString(key);
            System.out.println(movement);
            processInput(movement) ;
        }
    }


    private void saveAndQuit() {

        File fileLocation = new File("save.txt");
        //DrawRandomMap map2 = new DrawRandomMap(80, 40, map.seed);
        //Game game = new Game();
        try {
            FileOutputStream fileOut = new FileOutputStream(fileLocation);
            ObjectOutputStream output = new ObjectOutputStream(fileOut);

            output.writeObject(world);
            output.writeObject(playerX);
            output.writeObject(playerY);


            output.close();
            fileOut.close();
        } catch(IOException e){
            System.out.println("debug me");
      }
        drawFrame("Successfully saved!", "");
        StdDraw.pause(1000);
        //Reset Setup mode to false
        setupMode = false;
        System.exit(0);
     }

     private void load() throws IOException{

         //drawFrame("Loading previous game", "");
         File fileLocation = new File("save.txt");
         try{
             FileInputStream fileIn = new FileInputStream(fileLocation);
             ObjectInputStream in = new ObjectInputStream(fileIn);
             //map = (DrawRandomMap) in.readObject();
             world = (TETile[][]) in.readObject();
             playerX = (Integer) in.readObject();
             playerY = (Integer) in.readObject();

             drawFrame("Loading previous game", "");
             StdDraw.pause(1000);
           //  world = map.generateWorld();
             //draw previous player position
             //world[map.playerX][map.playerY] = Tileset.PLAYER;
             ter.renderFrame(world);
         } catch(IOException | ClassNotFoundException i){
             System.out.println(i);
         }
         //try quitAndSave method, checks for q input.
         //processInput(keyboardInput());
         processInputRecursively();
     }



    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        int sizeOfInput = input.length();

        String seedToUse = input.substring(1,sizeOfInput - 1);
        //System.out.println(seedToUse);

        long seed = Long.parseLong(seedToUse);
        DrawRandomMap newMap = new DrawRandomMap(80,40,seed);
        TETile[][] finalWorldFrame = newMap.generateWorld();
        return finalWorldFrame;
    }


}
