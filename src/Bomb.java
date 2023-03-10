import bagel.Image;


/**
 * This class handle all the Bomb relating action. For example, construct a Bomb, attack relating actions
 */
public class Bomb extends Stationary{
    //image for bomb
    private final Image BOMB_EXPLOISION_IMAGE = new Image("res/explosion.png");

    //bomb's damage to the sailor if collision between them has occurred
    private final int DAMAGE = 10;

    //bomb's state relating constants and variables
    private String state = "attack";
    private int renderPriod = 0;
    private final int MAX_RENDER_PERIOD = 30;

    /**
     * Constructor for the bomb
     */
    public Bomb(int positionx, int positiony) {
        super(positionx, positiony, new Image("res/bomb.png"));
    }

    /**
     * This function is responsible for all bomb attacking sailor's logic
     * 1. update the current image in the super class
     * 2. check whether the renderperiod has passed
     * 3. if passed, return true the outer function should remove the bomb from the list
     */
    public boolean attack(){
        if(renderPriod < MAX_RENDER_PERIOD){
            super.setCurrImage(BOMB_EXPLOISION_IMAGE);
        } else {
            return true;
        }
        return false;
    }

    /**
     * Getter for the renderPeriod
     */
    public int getRenderPeriod() {
        return renderPriod;
    }

    /**
     * Setter for the renderPeriod
     */
    public void setRenderPeriod(int renderPeriod) {
        this.renderPriod = renderPeriod;
    }

    /**
     * Getter for the state
     */
    public String getState() {
        return state;
    }

    /**
     * Setter for the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter for the DAMAGE
     */
    public int getDAMAGE() {
        return DAMAGE;
    }

}
