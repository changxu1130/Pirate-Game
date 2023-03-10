import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Character is a super class for Sailor and Pirate, handle the render, createRectangle etc. functions for them
 * Character also implements Movable, Renderable, Damagable, Attackable interfaces
 */
public abstract class Character implements Movable, Renderable, Damagable, Attackable{

    //Position relating constants and variables
    private double positionx;
    private double positiony;
    private final double CURRSPEED;

    //Direction relating constants and variables
    private static final String DIREC_L = "left";
    private static final String DIREC_R = "right";
    private static final String DIREC_U = "up";
    private static final String DIREC_D = "down";
    private static final String INITIAL_DIRECTION = "right";
    private String currDirection;

    //health point percentage display on the top of the window relating constants
    private final static int DECIMAL_2_PERCENTAGE = 100;
    private final static String PERCENTAGE_SYMBOL = "%";

    //colours might need to double check this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private final static double HEALTH_POINT_PERCENT_ORANGE = 0.65;
    private final static double HEALTH_POINT_PERCENT_RED = 0.35;
    private final static DrawOptions ORANGE = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private final static DrawOptions RED = new DrawOptions().setBlendColour(1, 0, 0);
    private final static DrawOptions GREEN = new DrawOptions().setBlendColour(0, 0.8, 0.2);

    /**
     * Character constructor
     */
    public Character(int positionx, int positiony, double CURRSPEED){
        this.positionx = positionx;
        this.positiony = positiony;
        this.CURRSPEED = CURRSPEED;
        this.currDirection = INITIAL_DIRECTION;
    }

    /**
     * This function update the object's position based on the current direction
     */
    public void movementUpdate(String direction){

        //update object's current position
        if (direction.equals(DIREC_L)) {
            this.positionx -= CURRSPEED;
            this.currDirection = "left";
        }
        if (direction.equals(DIREC_R)) {
            this.positionx += CURRSPEED;
            this.currDirection = "right";
        }
        if (direction.equals(DIREC_U)) {
            this.positiony -= CURRSPEED;
            this.currDirection = "up";
        }
        if (direction.equals(DIREC_D)) {
            this.positiony += CURRSPEED;
            this.currDirection = "down";
        }

    }

    /**
     * This function returns a rectangle based on current sailor's position
     */
    public Rectangle createRectangle (Image currentImage){
        Point position = new Point(positionx, positiony);
        return new Rectangle(position, currentImage.getWidth(), currentImage.getHeight());
    }

    /**
     * This function moves back to the original position before collision, if collision detected
     */
    public void bounceBack(){

        if(currDirection.equals("right")){
            setPositionx(positionx - CURRSPEED);
        } else if (currDirection.equals("left")){
            setPositionx(positionx + CURRSPEED);
        } else if (currDirection.equals("up")){
            setPositiony(positiony + CURRSPEED);
        } else if (currDirection.equals("down")){
            setPositiony(positiony - CURRSPEED);
        }
    }

    /**
     * This function prints the HealthBar based on the location you want it to be displayed on the screen
     * int x and y: coordinate of where you want the healthbar to display
     */
    public void displayHealthBar(int fontSize, int currHealthPoint, int maxHealthPoint, double x, double y) {
        //format the health_point percentage //HEALTH_PERCENTAGE_FONT_SIZE = 30; DECIMAL_2_PERCENTAGE = 100;
        Font font = new Font("res/wheaton.otf", fontSize);
        long health_percentage = Math.round((double)currHealthPoint / maxHealthPoint * DECIMAL_2_PERCENTAGE);
        String health_percent_String =  Long.toString(health_percentage) + PERCENTAGE_SYMBOL;

        //set the colour to orange if health_percent drops below 65%, to red if below 35%, otherwise keep it green
        if(health_percentage < HEALTH_POINT_PERCENT_ORANGE * DECIMAL_2_PERCENTAGE
                && health_percentage > HEALTH_POINT_PERCENT_RED * DECIMAL_2_PERCENTAGE){
            font.drawString(health_percent_String,x, y, ORANGE);
        } else if (health_percentage < HEALTH_POINT_PERCENT_RED * DECIMAL_2_PERCENTAGE){
            font.drawString(health_percent_String, x, y, RED);
        } else {
            font.drawString(health_percent_String, x, y, GREEN);
        }
    }

    /**
     * Getters for positionx
     */
    public double getPositionx() {
        return positionx;
    }

    /**
     * Getters for positiony
     */
    public double getPositiony() {
        return positiony;
    }

    /**
     * Setters for positionx
     */
    public void setPositionx(double positionx) {
        this.positionx = positionx;
    }

    /**
     * Setters for positiony
     */
    public void setPositiony(double positiony) {
        this.positiony = positiony;
    }

    /**
     * Getters for currDirection
     */
    public String getCurrDirection() {
        return currDirection;
    }

}
