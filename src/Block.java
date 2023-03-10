import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block {
    private final Image BLOCK_IMAGE = new Image("res/block.png"); //image for the block
    //block's position (final because it doesn't need to be changed, once initialised)
    public final int POSITION_X;
    private final int POSITION_Y;

    /**
     * Block constructor
     */
    public Block(int x, int y){
        this.POSITION_X = x;
        this.POSITION_Y = y;
    }

    /**
     * print the block
     */
    public void printBlock(){
        BLOCK_IMAGE.draw(this.POSITION_X, this.POSITION_Y);
    }

    /**
     * return a rectangle based on a block's position
     */
    public Rectangle createRectangle (){
        return BLOCK_IMAGE.getBoundingBoxAt(new Point(this.POSITION_X, this.POSITION_Y));
    }
}
