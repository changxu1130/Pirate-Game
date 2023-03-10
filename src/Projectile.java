import bagel.Image;
import java.lang.Math;
import bagel.DrawOptions;

/**
 * This class is used to handle all the projectile (including the one fired by the Blackbeard) relating actions.
 * It implements Renderable, Movable, Attackable interfaces
 */
public class Projectile implements Renderable, Movable, Attackable {

    //Image relating variable and constants
    private Image currImage;
    private final Image PROJECTILE_PIRATE_IMAGE = new Image("res/pirate/pirateProjectile.png");
    private final Image PROJECTILE_BLACKBEARD_IMAGE = new Image("res/blackbeard/blackbeardProjectile.png");

    //Movement relating constants and variables
    private final double BASE_SPEED = 0.4;
    private final int BASE_DAMAGE = 10;
    private final static int BLACKBEARD_MULTIPLIER = 2; //used to convert attributes relating to pirate to blackbeard
    private double positionX;
    private double positionY;
    private double currentSpeed;

    private int currDamage;
    private double sailorX;
    private double sailorY;
    private double direction;

    /**
     * Constructor for the Projectile
     */
    public Projectile(double pirateX, double pirateY, double sailorX, double sailorY, boolean isBlackbeard){
        if(isBlackbeard){
            this.currentSpeed = BASE_SPEED * BLACKBEARD_MULTIPLIER;
            this.currImage = PROJECTILE_BLACKBEARD_IMAGE;
            this.currDamage = BASE_DAMAGE * BLACKBEARD_MULTIPLIER;
        } else {
            this.currentSpeed = BASE_SPEED;
            this.currImage = PROJECTILE_PIRATE_IMAGE;
            this.currDamage = BASE_DAMAGE;
        }
        //initiation of other variables
        this.sailorX = sailorX;
        this.sailorY = sailorY;
        this.positionX = pirateX;
        this.positionY = pirateY;
        //calculate the direction of the projectile based on the instruction provided on the project
        this.direction = Math.atan2((sailorY - this.positionY), (sailorX - this.positionX));
    }


    /**
     * This function handles all the attack relating actions
     */
    @Override
    public void attack() {

    }

    /**
     * This function is responsible for calculating the position of the projectile based on the position of the pirate
     * who fired it and the sailor
     */
    @Override
    public void move() {
    }

    /**
     * This function renders the projectile image to the screen
     */
    @Override
    public void render() {
        //setRotation(double rotation)
        currImage.drawFromTopLeft(positionX,positionY, new DrawOptions().setRotation(direction));

    }

    /**
     * This function updates the position of hte projectile every frame
     */
    public void update(){
        this.positionX += Math.cos(direction) * currentSpeed;
        this.positionY += Math.sin(direction) * currentSpeed;
    }

    /**
     * Getter for positionX
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * Getter for positionY
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * Getter for currImage
     */
    public Image getCurrImage() {
        return currImage;
    }

    /**
     * Getter for currDamage
     */
    public int getCurrDamage() {
        return currDamage;
    }
}

