import bagel.*;
import java.util.Random;

/**
 * Level is a super class for Level1 and Level0, handle the get random speed etc. functions for them
 */
public class Level {

    //Font size and default Y coordinate of all bottom left message
    private final static String DEFAULT_FONT = "res/wheaton.otf";
    private final static int DEFAULT_MESSAGE_FONT_SIZE = 55;
    private final static int DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE = 402;
    private final static int DEFAULT_SPACE_BETWEEN_MESSAGES = 70;

    //Movement relating constants and variables
    private final static int RANDOM_DIREC = 4;
    private final static int DIREC_R = 0;
    private final static int DIREC_L = 1;
    private final static int DIREC_U = 2;
    private final static int DIREC_D = 3;

    /**
     * Method used to read file and create objects
     */
    protected void readcsv(String filename){
    }

    /**
     * This function renders the game instruction messages on screen
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
     * This function is used to generate a random direction
     */
    public String getRandDirec(){
        //randomly generate a number out of 0, 1, 2, 3, with each corresponding to a direction, right, left, up, down
        Random rand = new Random(); //instance of random class

        //generate random values from 0-3 (inclusive)
        int int_random = rand.nextInt(RANDOM_DIREC);
       // System.out.printf("%d\n", int_random);

        if (int_random == DIREC_L) {
            return "left";
        }
        if (int_random == DIREC_R) {
            return "right";
        }
        if (int_random == DIREC_U) {
            return "up";
        }
        else {
            return "down";
        }
    }

    /**
     * This function is used to generate a random speed from 0.2 to 0.7
     */
    public double getRandSpeed(){
        //randomly generate a double from 0.2 to 0.7 inclusive
        //double speed = (0.5 * Math.random()) + 0.2;
        //System.out.printf("random speed = %f\n", speed);
        return (0.5 * Math.random()) + 0.2;
    }


    /**
     * This function checks whether the movable entities are out of the screen boundary
     */
    public boolean OutOfBound(double x, double y, int minX, int maxX, int minY, int maxY) {
        //System.out.printf("maxX = %d\n", maxX);
        //System.out.printf("positionx = %f\n", positionx);
        if(x > maxX ){
            //System.out.println("maxX");
            return true;
        }
        if(x < minX){
            //System.out.println("minx");
            return true;
        }
        if(y > maxY){
            //System.out.println("maxY");
            return true;
        }
        if(y < minY){
            // System.out.println("minX");
            return true;
        }
        return false;
    }

}
