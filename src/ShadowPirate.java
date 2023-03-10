import bagel.*;
import bagel.Image;
import bagel.Window;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2022
 *
 * Please fill your name below
 * @XuChang
 */
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final Image BACKGROUND0_IMAGE = new Image("res/background0.png");
    private final Image BACKGROUND1_IMAGE = new Image("res/background1.png");


    //Level relating attributes and constants
    private final Level0 level0 = new Level0();
    private final Level1 level1 = new Level1();

    //Level relating variable and constants
    private final static int LEVEL0_START = 0;
    private final static int LEVEL1_START = 1;

    //Current game state relating variable and constants
    private final static String MENU = "MENU";
    private final static String LEVEL_RUNING = "RUNNING";
    private final static String LEVEL_WIN = "WIN";
    private final static String LEVEL_LOSE= "LOSE";


    //initialise gameState, which is used to differentiate different stages of the game, menu, running, end etc.
    private final GameState gameState = new GameState(0, MENU);


    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {


        //set up the Level0
        if(this.gameState.getCurrLevel() == LEVEL0_START){

            //print out the message to signify the start of the game
            if(this.gameState.getCurrState().equals(MENU)){
                this.level0.level0Menu();

                //update the gamestate to running if space is pressed
                if (input.wasPressed(Keys.SPACE)){
                    this.gameState.setCurrState(LEVEL_RUNING);
                }
            }
            if(this.gameState.getCurrState().equals(LEVEL_RUNING)){
                //update the sailor's position based on the key entered by the user, and draw on the screen
                BACKGROUND0_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

                //detects whether the end of the level has reached
                if(this.level0.level0Running(input).equals(LEVEL_WIN)){
                    this.gameState.setCurrState(LEVEL_WIN);
                }
                if(this.level0.level0Running(input).equals(LEVEL_LOSE)){
                    this.gameState.setCurrState(LEVEL_LOSE);
                }

            }
            //level0Win will return true if the winning message has been displayed for long enough ,
            // 1. gamestate's currentState should changed to "MENU",
            // 2. gamestate's currLevel should changed to 1
            if(this.gameState.getCurrState().equals(LEVEL_WIN)){
                this.level0.setWinMessageDisplayPeriod(level0.getWinMessageDisplayPeriod() + 1);
                if(!this.level0.level0Win()){
                    this.gameState.setCurrLevel(1);
                    this.gameState.setCurrState(MENU);
                }
            }

            //close the window if the end of the level has reached
            if(this.gameState.getCurrState().equals(LEVEL_LOSE)){
                this.level0.level0Lose();
            }
        }

        //set up the Level1
        if(this.gameState.getCurrLevel() == LEVEL1_START){
            //print out the message to signify the start of the game
            if(this.gameState.getCurrState().equals(MENU)){
                this.level1.level1Menu();

                //update the gamestate to running if space is pressed
                if (input.wasPressed(Keys.SPACE)){
                    this.gameState.setCurrState(LEVEL_RUNING);
                }
            }
            if(this.gameState.getCurrState().equals(LEVEL_RUNING)){
                //update the sailor's position based on the key entered by the user, and draw on the screen
                BACKGROUND1_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

                //detects whether the end of the level has reached
                if(this.level1.level1Running(input).equals(LEVEL_WIN)){
                    this.gameState.setCurrState(LEVEL_WIN);
                }
                if(this.level1.level1Running(input).equals(LEVEL_LOSE)){
                    this.gameState.setCurrState(LEVEL_LOSE);
                }
            }

            //close the window if the end of the level has reached
            if(this.gameState.getCurrState().equals(LEVEL_WIN)){
                 this.level1.level1Win();
            }
            if(this.gameState.getCurrState().equals(LEVEL_LOSE)){
                this.level1.level1Lose();
            }
        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }

}

