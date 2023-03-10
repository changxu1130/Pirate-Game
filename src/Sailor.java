import bagel.*;
import java.util.ArrayList;


/**
 * This class handle all the Sailor relating action. For example, construct a Sailor, Attack and Damage relating actions
 */
public class Sailor extends Character implements Movable, Attackable, Damagable {
    //images for sailor
    private final Image SAILOR_IMAGE_L = new Image("res/sailor/sailorLeft.png");
    private final Image SAILOR_IMAGE_R = new Image("res/sailor/sailorRight.png");
    private final Image SAILOR_ATTACK_L = new Image("res/sailor/sailorHitLeft.png");
    private final Image SAILOR_ATTACK_R = new Image("res/sailor/sailorHitRight.png");
    private Image currImage;
    private Image leftImage;
    private Image rightImage;

    private Input input;
    //private String direction = "right";
    private final static double SPEED = 1.0;

    //Sailor's state relating variables and constants
    private String sailorState;
    private final static String IDELE = "IDLE";
    private final static String ATTACK = "ATTACK";
    private final static String COOLDOWN = "COOLDOWN";
    private final int MAX_ATTACK_PERIOD = 60;
    private final int MIN_COOLDOWN_PERIOD  = 120;

    private int attackPeriod;
    private int cooldownPeriod;

    //health_point relating variables or constants
    private int maxHealthPoint = 100;
    private final static int INITIAL_DAMAGE_POINT = 15;

    private int currDamage;
    private int currHealthPoint = 100;
    //the integer variable required for the log collideDamage, health_point, MAX_HEALTH_POINT

    //health point percentage display on the top of the window relating constants
    private final static int HEALTH_PERCENTAGE_FONT_SIZE = 30;
    private final static int HEALTH_PERCENTAGE_POSITION_X = 10;
    private final static int HEALTH_PERCENTAGE_POSITION_Y = 25;

    //inventory relating constants
    private final static int INVENTORY_Y = 40;
    private final static int INVENTORY_Y_DISTANCE = 43;

    private final ArrayList<Image> inventoryIcons = new ArrayList<>();

    public void addIcons(Image icon){
        this.inventoryIcons.add(icon);
    }

    /**
     * Sailor constructor
     */
    public Sailor(int x, int y) {
        super(x, y, SPEED);
        this.sailorState = IDELE;
        this.currImage = SAILOR_IMAGE_R;
        this.leftImage = SAILOR_IMAGE_L;
        this.rightImage = SAILOR_IMAGE_R;
        this.currDamage = INITIAL_DAMAGE_POINT;
    }

    /**
     * Attack function aims to handle all the relevant operations regarding sailor attacking pirates
     * operations include: 1. state detection, 2. display image changes, 3. start counting attack period
     */
    @Override
    public void attack(){
        // make sure the sailor's state is not cooldown, and then change the current image into attack
        //System.out.println("inside attack");
        cooldownDetect();
        if(!sailorState.equals(COOLDOWN)){
            this.leftImage = SAILOR_ATTACK_L;
            this.rightImage = SAILOR_ATTACK_R;

            //make sure the image is rendered correctly even up and down key is pressed
            if(this.getCurrDirection().equals("up") || this.getCurrDirection().equals("down")){
                if(this.currImage.equals(SAILOR_IMAGE_L)){
                    this.currImage = SAILOR_ATTACK_L;
                }
                if(this.currImage.equals(SAILOR_IMAGE_R)){
                    this.currImage = SAILOR_ATTACK_R;
                }

            }
            setSailorState(ATTACK);
            this.attackPeriod += 1;
        }
    }

    /**
     * This helper function checks the timer and see if the attack mode has last for 1000 ms
     * and counts down the cooldown time
     */
    public void cooldownDetect(){
        //if the attackPeriod of the sailor achieve 1000 ms
        //System.out.println("inside cooldown");
        if (attackPeriod == MAX_ATTACK_PERIOD){
            //change the sailorState into COOLDOWN
            setSailorState(COOLDOWN);
            //reset the attackPeriod and start the cooldown period
            attackPeriod = 0;
            cooldownPeriod += 1;
        }
        // if the sailor's cooldown period is larger than 2000 ms switch the state back to idle
        if (cooldownPeriod > MIN_COOLDOWN_PERIOD){
            setSailorState(IDELE);
            cooldownPeriod = 0;
          // else if the sailor is cooling down, change its image to match the state
        } else if (cooldownPeriod > 0){
            this.leftImage = SAILOR_IMAGE_L;
            this.rightImage = SAILOR_IMAGE_R;

            if(this.getCurrDirection().equals("up") || this.getCurrDirection().equals("down")){
                if(this.currImage.equals(SAILOR_ATTACK_L)){
                    this.currImage = SAILOR_IMAGE_L;
                }
                if(this.currImage.equals(SAILOR_ATTACK_R)){
                    this.currImage = SAILOR_IMAGE_R;
                }

            }
        }
    }

    /**
     * This function is extended from the interface Damagable and drops the sailor's healthpoint based on the damage
     */
    @Override
    public void damage(int damage){
        //if the pirate is in the attack state, and collide with the sailor, the sailor will have the pirate's full
        // damage points inflicted to him
        //drop the healthpoint by the damage entered
        setCurrHealthPoint(getCurrHealthPoint() - damage);
    }

    /**
     * This function updates the position of the sailor based on the input from player
     */
    @Override
    public void move() {

        //update sailor's current position based on the key pressed by user
        if (this.input.isDown(Keys.LEFT)) {
            super.movementUpdate("left");
        }
        else if (this.input.isDown(Keys.RIGHT)) {
            super.movementUpdate("right");
        }
        else if (this.input.isDown(Keys.UP)) {
            super.movementUpdate("up");
        }
        else if (this.input.isDown(Keys.DOWN)) {
            super.movementUpdate("down");
        }

        //update the image based on the direction entered remember to
        if (super.getCurrDirection().equals("right")) {
            currImage = rightImage;
            //sailorImageR.draw(position_x, position_y);
        } else if (super.getCurrDirection().equals("left")) {
            currImage = leftImage;
            //sailorImageL.draw(position_x, position_y);
        }
    }

    /**
     * This function renders the image of the sailor and the inventories arraylist (for Level1) to the screen
     */
    @Override
    public void render() {
        int positiony = INVENTORY_Y;
        currImage.drawFromTopLeft(super.getPositionx(), super.getPositiony());
        for (Image inventoryIcon : inventoryIcons) {
            inventoryIcon.drawFromTopLeft(0, positiony);
            positiony += INVENTORY_Y_DISTANCE;
        }
    }

    /**
     * This function prints the health point
     */
    public void drawHealthPoint(){
        super.displayHealthBar(HEALTH_PERCENTAGE_FONT_SIZE, currHealthPoint, maxHealthPoint,
                                                HEALTH_PERCENTAGE_POSITION_X, HEALTH_PERCENTAGE_POSITION_Y);
    }

    /**
     * Getter for maxHealthPoint
     */
    public int getMaxHealthPoint() {
        return maxHealthPoint;
    }

    /**
     * Setter for maxHealthPoint
     */
    public void setMaxHealthPoint(int maxHealthPoint) {
        this.maxHealthPoint = maxHealthPoint;
    }

    /**
     * Getter for sailor's current healthpoint
     */
    public int getCurrHealthPoint() {
        return currHealthPoint;
    }

    /**
     * Setter for sailor's current healthpoint
     */
    public void setCurrHealthPoint(int currHealthPoint) {
        this.currHealthPoint = currHealthPoint;
    }

    /**
     * Getter for current sailorState
     */
    public String getSailorState() {
        return sailorState;
    }

    /**
     * Setter for current sailorState
     */
    public void setSailorState(String sailorState) {
        this.sailorState = sailorState;
    }

    /**
     * Getter for current cooldownPeriod
     */
    public int getCooldownPeriod() {
        return cooldownPeriod;
    }

    /**
     * Setter for current cooldownPeriod
     */
    public void setCooldownPeriod(int cooldownPeriod) {
        this.cooldownPeriod = cooldownPeriod;
    }

    /**
     * Getter for current damage
     */
    public int getCurrDamage() {
        return currDamage;
    }

    /**
     * Setter for current damage
     */
    public void setCurrDamage(int currDamage) {
        this.currDamage = currDamage;
    }

    /**
     * Setter for the input
     */
    public void setInput(Input input) {
        this.input = input;
    }

    /**
     * Getter for currImage
     */
    public Image getCurrImage() {
        return currImage;
    }


}