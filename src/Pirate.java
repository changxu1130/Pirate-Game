import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * This class is used to handle all the pirate (including blackbeard) relating actions
 */
public class Pirate extends Character implements Movable, Damagable, Attackable{

    //images for pirate
    private final Image PIRATE_IMAGE_L = new Image("res/pirate/pirateLeft.png");
    private final Image PIRATE_IMAGE_R = new Image("res/pirate/pirateRight.png");
    private final Image PIRATE_INVINCIBLE_L = new Image("res/pirate/pirateHitLeft.png");
    private final Image PIRATE_INVINCIBLE_R = new Image("res/pirate/pirateHitRight.png");

    //images for blackbeard
    private final Image BLACKBEARD_IMAGE_L = new Image("res/blackbeard/blackbeardLeft.png");
    private final Image BLACKBEARD_IMAGE_R = new Image("res/blackbeard/blackbeardRight.png");
    private final Image BLACKBEARD_INVINCIBLE_L = new Image("res/blackbeard/blackbeardHitLeft.png");
    private final Image BLACKBEARD_INVINCIBLE_R = new Image("res/blackbeard/blackbeardHitRight.png");

    //images for the current object, which might be the Blackbeard or a normal Pirate
    private Image leftImage = PIRATE_IMAGE_L;
    private Image rightImage = PIRATE_IMAGE_R;
    private Image currImage = rightImage;

    //used to convert attributes relating to pirate to blackbeard
    private final static int BLACKBEARD_MULTIPLIER = 2;
    private final boolean IS_BLACKBEARD;

    //object's state relating constants and variables
    private String attackState; //denotes the current state of the object
    private final static String READY_TO_ATTACK = "READY_TO_ATTACK";

    //damagable should be set to false if the pirate just get attacked by the sailor
    //damagable's default value is set to true
    private boolean damagble = true;

    //private int attackPeriod;
    private int cooldownPeriod;
    private int invinciblePeriod;
    private final static int MAX_COOLDOWN_PERIOD = 180;
    private final static int MAX_INVINCIBLE_PERIOD = 90;

    private final static int BASE_ATTACK_RANGE = 100;
    private final int CURR_ATTACK_RANGE;

    //movement relating constants and variables
    private String currDirection;

    //healthpoint relating constants and variables
    private final static int BASE_HEALTH_POINT = 45;
    private final int maxHealthPoint;
    private int currentHealthPoint;

    //health point percentage display on the top of the image
    private final static int HEALTH_PERCENTAGE_FONT_SIZE = 15;

    /**
     * Sailor's constructor
     */
    public Pirate(int x, int y, double currSpeed, String currDirection, boolean IS_BLACKBEARD){
        super(x, y, currSpeed);

        this.IS_BLACKBEARD = IS_BLACKBEARD;

        //initialise the image into blackbeard if the pirate is blackbeard and
        // establish the maxHealthPoint depending on the pirate type
        if(IS_BLACKBEARD) {
            this.leftImage = BLACKBEARD_IMAGE_L;
            this.rightImage = BLACKBEARD_IMAGE_R;

            this.maxHealthPoint = BASE_HEALTH_POINT * BLACKBEARD_MULTIPLIER;
            this.CURR_ATTACK_RANGE = BASE_ATTACK_RANGE * BLACKBEARD_MULTIPLIER;
            this.currImage = rightImage;

        } else {
            this.maxHealthPoint = BASE_HEALTH_POINT;
            this.CURR_ATTACK_RANGE = BASE_ATTACK_RANGE;
        }

        //initialisation of other variables
        this.attackState = READY_TO_ATTACK;
        this.currDirection = currDirection;
        this.currentHealthPoint = maxHealthPoint;
    }

    /**
     * This function updates the position of the pirates
     */
    @Override
    public void move() {
        super.movementUpdate(currDirection);

        //update the image based on the direction determined upon creation
        if (super.getCurrDirection().equals("right")) {
            currImage = rightImage;
            //sailorImageR.draw(position_x, position_y);
        } else if (super.getCurrDirection().equals("left")) {
            currImage = leftImage;
            //sailorImageL.draw(position_x, position_y);
        }
    }

    /**This function changes pirate's currDirection to the opposite if collision with block or boundary is detected
     */
    public void changeCurrDirection(){
        switch (super.getCurrDirection()) {
            case "right":
                //System.out.printf("change the direction from R to L\n");
                setCurrDirection("left");
                break;
            case "left":
                //System.out.printf("change the direction from L to R\n");
                setCurrDirection("right");
                break;
            case "up":
                //System.out.printf("change the direction from U to D\n");
                setCurrDirection("down");
                break;
            case "down":
                //System.out.printf("change the direction from D to U\n");
                setCurrDirection("up");
                break;
        }
    }


    /**
     * This function renders the image of the pirate to the screen
     */
    @Override
    public void render() {
        currImage.drawFromTopLeft(super.getPositionx(), super.getPositiony());
    }


    /**
     * This function is extended from the interface Damagable
     */
    @Override
    public void damage(int damage){
        //if the sailor is in the attack state, and collide with the pirates, these pirates will have the sailor's full
        // damage pooints inflicted to them
        //drop the healthpoint by the damage entered
        //sailor in attack state can only damage the pirate if the pirate is damagable
        if(damagble){
            setCurrentHealthPoint(getCurrentHealthPoint() - damage);
            setDamagble(false);
        }
    }


    /**
     * This function changes the image of the pirate to the invincibles as well as a timer
     * The pirate is always damageable unless it just got hit by a sailor
     * 1. if hit by the sailor, set its state to false
     * 2. else if the invincible period is greater than a value, set it back to true
     */
    public void invincible(){
        //getAttackState().equals(INVINCIBLE)
        if(!damagble){
            if(IS_BLACKBEARD){
                this.leftImage = BLACKBEARD_INVINCIBLE_L;
                this.rightImage = BLACKBEARD_INVINCIBLE_R;

            } else {
                this.leftImage = PIRATE_INVINCIBLE_L;
                this.rightImage = PIRATE_INVINCIBLE_R;
            }
            //setDamagble(false);
        }

        if (this.invinciblePeriod > MAX_INVINCIBLE_PERIOD){
            setDamagble(true);
            if(IS_BLACKBEARD){
                this.leftImage = BLACKBEARD_IMAGE_L;
                this.rightImage = BLACKBEARD_IMAGE_R;
            } else {
                this.leftImage = PIRATE_IMAGE_L;
                this.rightImage = PIRATE_IMAGE_R;
            }

            //this.setAttackState(READY_TO_ATTACK);
            invinciblePeriod = 0;
        }
    }

    /**
     * This function prints the health point
     */
    public void drawHealthPoint(){
        super.displayHealthBar(HEALTH_PERCENTAGE_FONT_SIZE,currentHealthPoint, maxHealthPoint,
                                                                super.getPositionx(), super.getPositiony());
    }

    /**
     * This function handles the attack logic for pirate
     */
    @Override
    public void attack(){
        //check if the current pirate is eligible to attack
        cooldownDetec();
    }

    /**
     * This function creates an attack range rectangle
     */
    public Rectangle createAttackRangeRectangle (Image currentImage){
        //get the point that is at the centre of the projectile
        Point position = new Point(super.getPositionx() + currentImage.getWidth() / 2,
                super.getPositiony() +  currentImage.getHeight() / 2);

        return new Rectangle(new Point(position.x - CURR_ATTACK_RANGE / 2, position.y - CURR_ATTACK_RANGE / 2),
                CURR_ATTACK_RANGE, CURR_ATTACK_RANGE);
    }

    /**
     * This function detects the cooldownPeriod and change the states if needed
     */
    public void cooldownDetec(){
        // if the sailor's cooldown period is larger than 2000 ms switch the state back to idle
        if (cooldownPeriod > MAX_COOLDOWN_PERIOD){
            setAttackState(READY_TO_ATTACK);
            cooldownPeriod = 0;
        }

    }

    /**
     * Setter for the currDirection
     */
    public void setCurrDirection(String currDirection) {
        this.currDirection = currDirection;
    }

    /**
     * Getter for currImage
     */
    public Image getCurrImage() {
        return currImage;
    }

    /**
     * Getter for currentHealthPoint
     */
    public int getCurrentHealthPoint() {
        return currentHealthPoint;
    }

    /**
     * Setter for currentHealthPoint
     */
    public void setCurrentHealthPoint(int currentHealthPoint) {
        this.currentHealthPoint = currentHealthPoint;
    }

    /**
     * Getter for maxHealthPoint
     */
    public int getMaxHealthPoint() {
        return maxHealthPoint;
    }

    /**
     * Getter for attackState
     */
    public String getAttackState() {
        return attackState;
    }

    /**
     * Setter for attackState
     */
    public void setAttackState(String attackState) {
        this.attackState = attackState;
    }

    /**
     * Getter for cooldownPeriod
     */
    public int getCooldownPeriod() {
        return cooldownPeriod;
    }

    /**
     * Setter for cooldownPeriod
     */
    public void setCooldownPeriod(int cooldownPeriod) {
        this.cooldownPeriod = cooldownPeriod;
    }

    /**
     * Getter for invinciblePeriod
     */
    public int getInvinciblePeriod() {
        return invinciblePeriod;
    }

    /**
     * Setter for invinciblePeriod
     */
    public void setInvinciblePeriod(int invinciblePeriod) {
        this.invinciblePeriod = invinciblePeriod;
    }

    /**
     * Getter for damagble
     */
    public boolean isDamagble() {
        return damagble;
    }

    /**
     * Setter for damagble
     */
    public void setDamagble(boolean damagble) {
        this.damagble = damagble;
    }

}
