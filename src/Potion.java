import bagel.Image;

/**
 * This class handle all the Potion relating action. For example, construct a Potion, pickUpEffect
 */
public class Potion extends Inventory{
    //Images for the Potion
    private final Image POTION_ICON = new Image("res/items/potionIcon.png");

    //PickUpEffect relating constant and Log message
    private final int INCREASED_HEALTHPOINT = 25;
    private final String LOG = "Sailor finds Potion. Sailor's current health: %d/%d\n";

    /**
     * Constructor for the Potion
     */
    public Potion(int positionx, int positiony) {
        super(positionx, positiony, new Image("res/items/potion.png"));
    }

    /**
     * This function is responsible to apply the pickUpEffect of Potion to the sailor
     */
    @Override
    public Image pickUpEffect(Character sailor){
        //increase the sailor's current healthpoint by INCREASED_HEALTHPOINT, if exceed the maximum healthpoint,
        //set the current healthpoint to the current maximum healthpoint
        if(((Sailor)sailor).getCurrHealthPoint() + INCREASED_HEALTHPOINT < ((Sailor)sailor).getMaxHealthPoint()){
            ((Sailor)sailor).setCurrHealthPoint(((Sailor)sailor).getCurrHealthPoint() + INCREASED_HEALTHPOINT);
        } else {
            ((Sailor)sailor).setCurrHealthPoint(((Sailor)sailor).getMaxHealthPoint());
        }
        //print out the log message
        System.out.printf(LOG, ((Sailor) sailor).getCurrHealthPoint(),
                ((Sailor) sailor).getMaxHealthPoint());
         return POTION_ICON;
    }
}
