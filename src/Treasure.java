import bagel.Image;

/**
 * This class handle all the Treasure relating action. For example, construct a Treasure
 */
public class Treasure extends Stationary{

    /**
     * Constructor for the Treasure
     */
    public Treasure(int positionx, int positiony) {
        super(positionx, positiony, new Image("res/treasure.png"));
    }

}
