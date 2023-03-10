/**
 * This class is used to denote the state of the game, which can be used to switch between different level of the game
 */
public class GameState {

    //Level relating variable and constants
    private int currLevel;

    //Current game state relating variable and constants
    private String currState;

    /**
     *  GameState constructor
     */
    public GameState(int currLevel, String currState){
        this.currLevel = currLevel;
        this.currState = currState;
    }

    /**
     * Getters for level
     */
    public int getCurrLevel() {
        return currLevel;
    }

    /**
     * Setters for level
     */
    public void setCurrLevel(int currLevel) {
        this.currLevel = currLevel;
    }

    /**
     * Getters for current_state
     */
    public String getCurrState() {
        return currState;
    }

    /**
     * Setters for current_state
     */
    public void setCurrState(String currState) {
        this.currState = currState;
    }




}
