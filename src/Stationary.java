import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Stationary is a super class for Treasure and Bomb, handle the render, createRectangle etc. functions for them
 */
public class Stationary implements Renderable{

    //position relating variables
    private int positionx;
    private int positiony;

    //images for the current object
    private Image currImage;


    /**
     * Constructor for Stationary
     */
    public Stationary(int positionx, int positiony, Image currImage){
        this.positionx = positionx;
        this.positiony = positiony;
        this.currImage = currImage;
    }

    /**
     * Getter for postionx
     */
    public int getPositionx() {
        return positionx;
    }

    /**
     * Getter for positiony
     */
    public int getPositiony() {
        return positiony;
    }


    /**
     * This function renders the iamge to the screen
     */
    public void render(){
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
     * Setter for the currImage
     */
    public void setCurrImage(Image currImage) {
        this.currImage = currImage;
    }
}
