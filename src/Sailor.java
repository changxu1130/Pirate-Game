import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;

public class Sailor {

    //images for sailor
    private final static Image SAILOR_IMAGE_L = new Image("res/sailorLeft.png");
    private final static Image SAILOR_IMAGE_R = new Image("res/sailorRight.png");
    private Image currImage;

    private int position_x; //x-coordinate of sailor's current position
    private int position_y; //y-coordinate of sailor's current position
    private String direction = "right";
    private final static double SPEED = 20;

    //health_point relating variables or constants
    private final static int MAX_HEALTH_POINT = 100;
    private final static int MIN_HEALTH_POINT = 0;
    private final static int COLLISION_DAMAGE = 10;
    private int health_point = 100;
    private final static int DAMAGE = 25;  // maximum damage they can cause in an attack
    private final static String LOG = "Block inflicts %d damage points on Sailor. Sailor’s current health: %d/%d";
    //the integer variable required for the log collideDamage, health_point, MAX_HEALTH_POINT

    //health point percentage display on the top of the window relating constants
    private final static int HEALTH_PERCENTAGE_FONT_SIZE = 30;
    private final static int DECIMAL_2_PERCENTAGE = 100;
    private final static int HEALTH_PERCENTAGE_POSITION_X = 10;
    private final static int HEALTH_PERCENTAGE_POSITION_Y = 25;
    private final static String PERCENTAGE_SYMBOL = "%";

    //colours
    private final static int HEALTH_POINT_ORANGE = 65;
    private final static int HEALTH_POINT_RED = 35;
    private final static DrawOptions ORANGE = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private final static DrawOptions RED = new DrawOptions().setBlendColour(1, 0, 0);
    private final static DrawOptions GREEN = new DrawOptions().setBlendColour(0, 0.8, 0.2);

    //wining detection relating constants
    private final static int LADDER_X = 990;
    private final static int LADDER_Y = 630;

    //out_of_bound check relating constants
    private final static int R_BOUND = 0;
    private final static int L_BOUND = Window.getWidth();
    private final static int UP_BOUND =670;
    private final static int BOTTOM_BOUND = 60;

    /**
     * Sailor constructor
     */
    public Sailor(int x, int y){
        this.position_x = x;
        this.position_y = y;
    }

    /**
     * Update the position
     */
    public void update(Input input) {

        //update sailor's current position based on the key pressed by user
        if (input.wasPressed(Keys.LEFT)) {
            this.position_x -= SPEED;
            this.direction = "left";
        }
        if (input.wasPressed(Keys.RIGHT)) {
            this.position_x += SPEED;
            this.direction = "right";
        }
        if (input.wasPressed(Keys.UP)) {
            this.position_y -= SPEED;
            this.direction = "up";
        }
        if (input.wasPressed(Keys.DOWN)) {
            this.position_y += SPEED;
            this.direction = "down";
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        //update the image based on the direction entered
        if(this.direction.equals("right")){
            currImage = SAILOR_IMAGE_R;
            //sailorImageR.draw(position_x, position_y);
        } else if (this.direction.equals("left")){
            currImage = SAILOR_IMAGE_L;
            //sailorImageL.draw(position_x, position_y);
        }
    }

    /**
     * draw the image of the sailor only when the updated position is valid (within the bound)
     */
    public void draw(){
        if(!isLosing()){
        currImage.draw(position_x, position_y);
        }
    }

    /**
     * print the health point
     */
    public void drawHealthPoint(){

        //format the health_point percentage //HEALTH_PERCENTAGE_FONT_SIZE = 30; DECIMAL_2_PERCENTAGE = 100;
        Font font = new Font("res/wheaton.otf", HEALTH_PERCENTAGE_FONT_SIZE);
        Long health_percentage = Math.round((double)this.health_point / MAX_HEALTH_POINT * DECIMAL_2_PERCENTAGE);
        String health_percent_String =  health_percentage.toString() + PERCENTAGE_SYMBOL;

        //set the colour to orange if health_percent drops below 65%, to red if below 35%, otherwise keep it green
        if(health_percentage < HEALTH_POINT_ORANGE && health_percentage > HEALTH_POINT_RED){
            font.drawString(health_percent_String, HEALTH_PERCENTAGE_POSITION_X, HEALTH_PERCENTAGE_POSITION_Y, ORANGE);
        } else if (health_percentage < HEALTH_POINT_RED){
            font.drawString(health_percent_String, HEALTH_PERCENTAGE_POSITION_X, HEALTH_PERCENTAGE_POSITION_Y, RED);
        } else {
            font.drawString(health_percent_String, HEALTH_PERCENTAGE_POSITION_X, HEALTH_PERCENTAGE_POSITION_Y, GREEN);
        }
    }

    /**
     * return a rectangle based on current sailor's position
     */
    public Rectangle createRectangle (){
        Point position = new Point(position_x, position_y);
        return currImage.getBoundingBoxAt(position);
    }

    /**
     * if collision detected, then move back to the original position before collision
     */
    public void bounceBack(){
        if(this.direction.equals("right")){
            this.position_x -= SPEED;
        } else if (this.direction.equals("left")){
            this.position_x += SPEED;
        } else if (this.direction.equals("up")){
            this.position_y += SPEED;
        } else if (this.direction.equals("down")){
            this.position_y -= SPEED;
        }
    }

    /**
     * health point drops by 10
     */
    public void dropHealthPoint(){
        this.health_point -= COLLISION_DAMAGE;
        //System.out.println("drop health point now");
        printLog();
    }

    /**
     * print the log every time the block inflicts damage on the sailor
     */
    public void printLog(){
        System.out.println(String.format(LOG ,COLLISION_DAMAGE, health_point, MAX_HEALTH_POINT));
    }

    /**
     * win detection: when the sailor reaches the destination, ladder
     */
    public boolean isWinning(){
        return this.position_x >= LADDER_X && this.position_y >= LADDER_Y;
    }

    /**
     * lose detection: either the health point has dropped to 0 or the sailor is out of bound
     */
    public boolean isLosing(){
        //check whether the health_point is above 0
        if(health_point == MIN_HEALTH_POINT){
            return true;
        }
        //check whether the sailor is out of bound
        if(this.position_y < BOTTOM_BOUND || this.position_y > UP_BOUND){
            return true;
        }
        return this.position_x < R_BOUND || this.position_x > L_BOUND;
    }
}