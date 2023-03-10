import bagel.Image;

/**
 * This class handle all the Sword relating action. For example, construct a Sword, pickUpEffect
 */
public class Sword extends Inventory{
    //Image for the Sword
    private final Image SWORD_ICON = new Image("res/items/swordIcon.png");

    //PickUpEffect relating constant and Log message
    private final int DAMAGE_INCREASE = 15;
    private final String LOG = "Sailor finds Sword. Sailor's damage points increased to %d\n";

    /**
     * Constructor for the Sword
     */
    public Sword(int positionx, int positiony) {
        super(positionx, positiony, new Image("res/items/sword.png"));
    }

    /**
     * This function is responsible to apply the pickUpEffect of Sword to the sailor
     */
    @Override
    public Image pickUpEffect(Character sailor){
        //increase the sailor's currDamage point by DAMAGE_INCREASE
        ((Sailor)sailor).setCurrDamage(((Sailor)sailor).getCurrDamage() + DAMAGE_INCREASE);
        //print out the log message
        System.out.printf(LOG, ((Sailor) sailor).getCurrDamage());
        return SWORD_ICON;
    }
}
