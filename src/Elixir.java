import bagel.Image;

/**
 * This class handle all the Elixir relating action. For example, construct an Elixir, pickUpEffect
 */
public class Elixir extends Inventory{

    //Image for the Elixir
    private final Image ELIXIR_ICON = new Image("res/items/elixirIcon.png");

    //PickUpEffect relating constant and Log message
    private final int INCREASED_HEALTHPOINT = 35;
    private final String LOG = "Sailor finds Elixir. Sailor's current health: %d/%d\n";

    /**
     * Constructor for the Elixir
     */
    public Elixir(int positionx, int positiony) {
        super(positionx, positiony, new Image("res/items/elixir.png"));
    }

    /**
     * This function is responsible to apply the pickUpEffect of Elixir to the sailor
     */
    @Override
    public Image pickUpEffect(Character sailor){
        //increase the maximum healthpoint of the sailor by INCREASED_HEALTHPOINT
        ((Sailor)sailor).setMaxHealthPoint(((Sailor)sailor).getMaxHealthPoint() + INCREASED_HEALTHPOINT);
        //and set the current healthpoint to the maximum healthpoint
        ((Sailor)sailor).setCurrHealthPoint(((Sailor)sailor).getMaxHealthPoint());
        //print out the log message
        System.out.printf(LOG, ((Sailor) sailor).getCurrHealthPoint(),
                ((Sailor) sailor).getMaxHealthPoint());
        return ELIXIR_ICON;
    }
}
