import bagel.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2022
 *
 * Please filling your name below
 * @XuChang
 */
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final static String INITIAL_POSITIONS = "res/level0.csv";

    //readcsv file relating constants (specify which column records the data of what)
    private final static int NAME_COL_NUM = 0;
    private final static int X_COORDINATE_COL_NUM = 1;
    private final static int Y_COORDINATE_COL_NUM = 2;
    private final static String SAILOR = "Sailor";
    private final static String BLOCK = "Block";

    //messages and its specific locations to print on screen
    private final static String START_MESSAGE1 = "PRESS SPACE TO START";
    private final static String START_MESSAGE2 = "USE ARROW KEYS TO FIND LADDER";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String END_OF_GAME_MESSAGE = "GAME OVER!";
    private final static String DEFAULT_FONT = "res/wheaton.otf";
    private final static int DEFAULT_MESSAGE_FONT_SIZE = 55;
    private final static int DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE = 402;
    private final static int DEFAULT_SPACE_BETWEEN_MESSAGES = 70;

    private String gameState = "menu";  //gameState is used to differentiate different stages of the game, playing,
                                        //lose, win, or end
    private Sailor sailor; // an instance of the sailor
    private final ArrayList<Block> blocks = new ArrayList<>(); // array list that stores all the blocks' position

    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);

        //read the csv file to initialise all the blocks and the sailor
        readcsv(INITIAL_POSITIONS);
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

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        //print out the message to signify the start of the game
        if(this.gameState.equals("menu")){
            printMessage(START_MESSAGE1);
            printMessage(START_MESSAGE2, DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE
                                                                                + DEFAULT_SPACE_BETWEEN_MESSAGES);
            if (input.wasPressed(Keys.SPACE)){
                this.gameState = "playing";
            }
        }

        //when the gameState is "playing"
        else if(this.gameState.equals("playing")){
            //update the sailor's position based on the key entered by the user, and draw on the screen
            this.sailor.update(input);
            this.sailor.draw();

            //print out all the blocks which have its position defined in the csv file
            for (Block block : blocks) {
                block.printBlock();
            }

            //check the conditions for the end of the game
            winDetection();
            loseDetection();

            //if collided, drop health point
            collision();
            this.sailor.drawHealthPoint();
        }

        //if the end of the game is reached, print out the message and exit
        else if(this.gameState.equals("win")){
            printMessage(WIN_MESSAGE);
        }
        else if(this.gameState.equals("lose")){
            printMessage(END_OF_GAME_MESSAGE);
        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }

    /**
     * win detection, update the gameState once all the conditions to win the game are met
     */
    public void winDetection(){
        if(this.sailor.isWinning()){
            this.gameState = "win";
        }
    }

    /**
     * lose detection, update the gameState once all the conditions to lose the game are met
     */
    public void loseDetection(){
        if(this.sailor.isLosing()){
            this.gameState = "lose";
        }
    }

    /**
     * Draw the game instruction messages on screen
     * Window.getWidth()/2.0 - font.getWidth(message)/2 calculates the x_coordinate required for the message to be
     * displayed at the centre of the window
     */
    public void printMessage(String message){
        Font font = new Font(DEFAULT_FONT, DEFAULT_MESSAGE_FONT_SIZE);
        //(Window.getWidth() - font.getWidth(message))/2
        // is to calculate the x coordinate requires displaying message in the middle of the screen
        font.drawString(message, (Window.getWidth() - font.getWidth(message))/2,
                DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE);
    }
    public void printMessage(String message, int y_coordinate){
        Font font = new Font(DEFAULT_FONT, DEFAULT_MESSAGE_FONT_SIZE);
        //(Window.getWidth() - font.getWidth(message))/2
        // is to calculate the x coordinate requires displaying message in the middle of the screen
        font.drawString(message, (Window.getWidth() - font.getWidth(message))/2, y_coordinate);
    }

    /**
     * Method used to read file and create objects
     */
    public void readcsv(String filename){
        try (BufferedReader br =
                     new BufferedReader(new FileReader(filename))) {
            String text;

            while ((text = br.readLine()) != null) {
                String cells[] = text.split(",");
                String name = cells[NAME_COL_NUM];
                int x_intercept = Integer.parseInt(cells[X_COORDINATE_COL_NUM]);
                int y_intercept = Integer.parseInt(cells[Y_COORDINATE_COL_NUM]);
                //System.out.format("%s need to be at %d, %d\n", name, x_intercept, y_intercept);

                //if the position recorded in the csv file if for sailor, create an instance of sailor at that point
                if(name.equals(SAILOR)){
                    //create a sailor based on the coordinates in world file
                    Sailor sailor = new Sailor(x_intercept, y_intercept);
                    this.sailor = sailor;
                }
                //if the position recorded in the csv file if for block, create an instance of block at that point
                else if (name.equals(BLOCK)){
                    //store all blocks' position in an array of blocks
                    blocks.add(new Block(x_intercept, y_intercept));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * detect collision via rectangle class, if detected drop the health point and bounce back
     */
    public void collision(){
        for (Block block : blocks) {
            //if there's a collision, health point drops and the sailor bounces back to the position before collision
            if (sailor.createRectangle().intersects(block.createRectangle())) {
                sailor.dropHealthPoint();
                sailor.bounceBack();
                break;
            }
        }
    }

}