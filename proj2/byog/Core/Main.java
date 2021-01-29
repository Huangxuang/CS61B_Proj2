package proj2.byog.Core;

import proj2.byog.TileEngine.TERenderer;
import proj2.byog.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80,50);
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString(args[0]);
            System.out.println(TETile.toString(worldState));
            ter.renderFrame(worldState);
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
