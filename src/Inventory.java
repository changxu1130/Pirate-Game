import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Inventory is a super class for Potion, Elixir and Sword, handle the render, createRectangle etc. functions for them
 * Inventory also implements Renderable interface
 */
public class Inventory implements Renderable {

    //Position and Image variables for the object 
    private int positionx;
    private int positiony;
    private Image currImage;

    /**
     * Constructor for the Inventory
     */
    public Inventory(int positionx, int positiony, Image currImage) {
        this.positionx = positionx;
        this.positiony = positiony;
        this.currImage = currImage;
    }

    /**
     * This function renders the currImage to the screen
     */
    @Override
    public void render() {
        this.currImage.drawFromTopLeft(this.positionx, this.positiony);
    }

    /**
     * This function returns a rectangle based on object's position
     */
    public Rectangle createRectangle (){
        Point position = new Point(positionx, positiony);
        return new Rectangle(position, currImage.getWidth(), currImage.getHeight());
    }

    /**
     * This function handles teh PickUpEffect relating actions
     */
    public Image pickUpEffect(Character sailor){
        return currImage;
    }
}
