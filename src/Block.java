import bagel.Image;

/**
 * This class handle all the Block relating action. For example, construct a Block
 */
public class Block extends Stationary{
    /**
     * Constructor for block
     */
    public Block(int positionx, int positiony){
        super(positionx, positiony, new Image("res/block.png"));
    }
}
