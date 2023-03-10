import bagel.*;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This class is used to handle all the Level0 relating actions
 */
public class Level0 extends Level{

    //readcsv file relating constants (specify which column records the data of what)
    private final static String INITIAL_POSITIONS = "res/level0.csv";
    private final static int NAME_COL_NUM = 0;
    private final static int X_COORDINATE_COL_NUM = 1;
    private final static int Y_COORDINATE_COL_NUM = 2;

    //level boundary coordinates
    private final static String BOUNDARY_TOP_LEFT = "TopLeft";
    private final static String BOUNDARY_BOTTOM_RIGHT = "BottomRight";
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    // list of all different kinds of objects that will be present in csv file
    private final static String SAILOR = "Sailor";
    private final static String PIRATE = "Pirate";
    private final static String BLOCK = "Block";

    // initiation of instance of sailor and arraylist of pirates, blocks
    private Sailor sailor;
    private final ArrayList<Pirate> pirates = new ArrayList<>(); // array list that stores all the pirates' position
    private final ArrayList<Block> blocks = new ArrayList<>(); // array list that stores all the blocks' position
    private final ArrayList<Projectile> projectiles = new ArrayList<>(); // array list that stores all the projectiles' position

    //messages and its specific locations to print on screen
    private final static String MESSAGE_START = "PRESS SPACE TO START";
    private final static String MESSAGE_ATTACK = "PRESS S TO ATTACK";
    private final static String MESSAGE_WIN_CONDITION_LEVEL0 = "USE ARROW KEYS TO FIND LADDER";
    private final static String MESSAGE_WIN_LEVEL0 = "LEVEL COMPLETE!";
    private final static String MESSAGE_LOSE = "GAME OVER";
    private final static int DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE = 402;
    private final static int DEFAULT_SPACE_BETWEEN_MESSAGES = 70;

    private int winMessageDisplayPeriod = 0;
    private final int MAX_WIN_MESSAGE_DISPLAY_PERIOD = 180;

    //pirates' state
    private final static String READY_TO_ATTACK = "READY_TO_ATTACK";
    private final static String COOLDOWN = "COOLDOWN";

    //sailor's sate
    private final static String ATTACK = "ATTACK";

    //level's state
    private final static String WIN = "WIN";
    private final static String LOSE = "LOSE";


    /**
     * Level0 constructor
     */
    public Level0(){
        //read the csv file to initialise all the blocks and the sailor
        readcsv(INITIAL_POSITIONS);
    }

    /**
     * level0Menu: print out all the start messages
     */
    public void level0Menu(){
        //print all the start messages
        super.printMessage(MESSAGE_START, DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE
                - DEFAULT_SPACE_BETWEEN_MESSAGES);
        super.printMessage(MESSAGE_ATTACK);
        super.printMessage(MESSAGE_WIN_CONDITION_LEVEL0, DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE
                + DEFAULT_SPACE_BETWEEN_MESSAGES);
    }

    /**
     * level0Running: perform all the actions required during the running state of the game
     */
    public String level0Running(Input input){

        //sailor's actions handling:

        //update the sailor's current position based on the input form user
        this.sailor.setInput(input);
        this.sailor.drawHealthPoint();

        //if the "S" is pressed, then work through the attack mode of the sailor
        if(input.wasPressed(Keys.S)){
            this.sailor.attack();
        }

        //if the sailorState is ATTACK, update the attack period
        if(this.sailor.getSailorState().equals(ATTACK)){
            this.sailor.attack();
            //detect collision between sailors and the pirates
            sailorCollideWithPirateDetect();
        }

        //if the sailorState is cooldown, update the cooldownPeriod
        if(this.sailor.getSailorState().equals("COOLDOWN")){
            this.sailor.setCooldownPeriod(this.sailor.getCooldownPeriod() + 1);
        }

        //update the sailor's position based on the input entered by user
        this.sailor.move();

        //only draw the sailor if the updated position is not out of bound
        if(!super.OutOfBound(sailor.getPositionx(), sailor.getPositiony(), minX, maxX, minY, maxY)){
            this.sailor.render();
        } else {
            this.sailor.bounceBack();
            this.sailor.render();
        }


        //pirates' actions handling

        //only draw the pirate if its position is not out of bound
        for (Pirate value : pirates) {
            value.move();
            //only update the move if the updated position is not out of bound
            if(!super.OutOfBound(value.getPositionx(), value.getPositiony(), minX, maxX, minY, maxY)){
                value.render();
                //if the updated position is out of bound bounce back
            } else {
                value.changeCurrDirection();
                value.render();
            }
            value.drawHealthPoint();
        }
        //remove the pirate from the screen if its healthpoint is below or equals to 0
        pirateDisappear();

        // if the pirate's state is READY_TO_ATTACK, fire projectile if the sailor is inside its attackrange
        for(Pirate value: pirates){
            value.attack();
            if(value.getAttackState().equals(READY_TO_ATTACK)){
                //if the sailor is inside its attackrange, pirate will fire one projectile and change state to COOLDOWN
                sailorWithinPirateAttackRange(value);
            }
            //if the pirate's current state is in COOLDOWN, update the CooldownPeriod timer
            if(value.getAttackState().equals(COOLDOWN)){
                value.setCooldownPeriod(value.getCooldownPeriod() + 1);
            }
            // if the pirate's current state is in INVINCIBLE, update the InvinciblePeriod timer
            if(!value.isDamagble()){
                value.invincible();
                value.setInvinciblePeriod(value.getInvinciblePeriod() + 1);
                System.out.printf("invinciblePeriod for the pirate is %d\n", value.getInvinciblePeriod()); //////////////////////////
            }
        }

        //print out all the projectiles which have its position defined
        for (Projectile value : projectiles) {
            value.render();
            value.update();
        }
        projectileCollideWithSailor();

        //interaction with the blocks handling

        //print out all the blocks which have its position defined in the csv file
        for (Block value : blocks) {
            value.render();
        }

        //detect the collision between blocks and sailor or pirate, and perform subsequent action
        sailorCollideWithBlock();
        pirateCollideWithBlock();

        //detect the current state of the game
        if(win()){
            return WIN;
        }else if(lose()){
            return LOSE;
        }
        return "";
    }



    /**
     * This function detects collision via rectangle class, if detected drop the health point and bounce back
     */
    public void sailorCollideWithBlock(){
        for (Block block : blocks) {
            //if there's a collision, the sailor bounces back to the position before collision
            if (sailor.createRectangle(sailor.getCurrImage()).intersects(block.createRectangle())) {
                sailor.bounceBack();
                break;
            }
        }
    }

    /**
     * This function detects collision via rectangle class, if detected drop the health point and bounce back
     */
    public void pirateCollideWithBlock(){
        for (Block block : blocks) {
            //if there's a collision, the pirate change to the opposite direction and keep moving
            for (Pirate value : pirates) {
                if (value.createRectangle(value.getCurrImage()).intersects(block.createRectangle())) {
                    //System.out.printf("collision between pirate and the block is detected \n");
                    value.changeCurrDirection();
                    break;
                }
            }
        }
    }

    /**
     * This function detects the attack range between the sailor and the pirate
     */
    public void sailorCollideWithPirateDetect(){
        for (Pirate value : pirates) {
            //System.out.printf("pirate damageable is %s\n", value.isDamagble());
            if (value.createRectangle(value.getCurrImage()).intersects(sailor.createRectangle(sailor.getCurrImage())) && value.isDamagble()) {
                value.damage(sailor.getCurrDamage());
                //print out the log message
                //log messages
                String LOG_SAILOR_TO_PIRATE = "Sailor inflict %d damage points on Pirate. Pirate's current health: %d/%d\n";
                System.out.printf(LOG_SAILOR_TO_PIRATE, sailor.getCurrDamage(),value.getCurrentHealthPoint(),
                        value.getMaxHealthPoint());
                value.setDamagble(false);
                value.invincible();
            }
        }
    }

    /**
     * This function detects whether the sailor is within pirate's attach range
     */
    public void sailorWithinPirateAttackRange(Pirate value){
        //createAttackRangeRectangle
        if (value.createAttackRangeRectangle(value.getCurrImage())
                .intersects(sailor.createRectangle(sailor.getCurrImage()))) {

            //add a projectile to the array list
            projectiles.add(new Projectile(value.getPositionx(), value.getPositiony(),
                    this.sailor.getPositionx(), this.sailor.getPositiony(), false));

            //change the state to COOLDOWN
            value.setAttackState(COOLDOWN);
        }
    }


    /**
     * This function detects whether the projectile has hit the pirate, if yes, it will
     * remove the projectile out of the arraylist, as well as the screen
     */
    public void projectileCollideWithSailor(){
        for(int i = 0; i < projectiles.size(); i++) {
            double projectCenterX = projectiles.get(i).getPositionX() + projectiles.get(i).getCurrImage().getWidth() / 2;
            double projectCenterY = projectiles.get(i).getPositionY() + projectiles.get(i).getCurrImage().getHeight() / 2;
            Point position = new Point(projectCenterX, projectCenterY);

            //detect whether the projectile hit the sailor as required in the project specifications
            if(sailor.createRectangle(sailor.getCurrImage()).intersects(position)){
                //inflict the damage to the sailor
                sailor.damage(projectiles.get(i).getCurrDamage());
                //print out the log message
                String LOG_PIRATE_TO_SAILOR = "Pirate inflicts %d damage points on Sailor. Sailor's current health: %d/%d\n";
                System.out.printf(LOG_PIRATE_TO_SAILOR,
                        projectiles.get(i).getCurrDamage(), sailor.getCurrHealthPoint(), sailor.getMaxHealthPoint());
                projectiles.remove(i);
                i--;
            }
        }
    }

    /**
     * Method used to read csv file and create objects
     */
    @Override
    protected void readcsv(String filename){
        try (BufferedReader br =
                     new BufferedReader(new FileReader(filename))) {
            String text;

            while ((text = br.readLine()) != null) {
                String cells[] = text.split(",");
                String name = cells[NAME_COL_NUM];
                int x_intercept = Integer.parseInt(cells[X_COORDINATE_COL_NUM]);
                int y_intercept = Integer.parseInt(cells[Y_COORDINATE_COL_NUM]);
                //System.out.format("%s need to be at %d, %d\n", name, x_intercept, y_intercept);

                // accept the level boundary from the csv file
                if(name.equals(BOUNDARY_TOP_LEFT)){
                    this.minX = x_intercept;
                    this.minY = y_intercept;
                    //System.out.printf("minX = %d, minY = %d\n", minX, minY);
                }
                if(name.equals(BOUNDARY_BOTTOM_RIGHT)){
                    this.maxX = x_intercept;
                    this.maxY = y_intercept;
                    //System.out.printf("maxX = %d, maxY = %d\n", maxX, maxY);
                }

                //create a sailor based on the coordinates in world file
                if(name.equals(SAILOR)){
                    this.sailor = new Sailor(x_intercept, y_intercept);
                }

                //create an array list of pirates based on the coordinates in world file ///////////////////////////////////////
                if(name.equals(PIRATE)){
                    pirates.add(new Pirate(x_intercept, y_intercept, super.getRandSpeed(), super.getRandDirec(), false));
                    //System.out.printf("create a pirate at location (%d, %d)\n", x_intercept, y_intercept);
                }

                //create an array list of blocks based on the coordinates in world file
                else if (name.equals(BLOCK)){
                     //store all blocks' position in an array list of blocks
                     blocks.add(new Block(x_intercept, y_intercept));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function handles the pirate disappearance from the screen after its healthpoint drop to 0
     */
    public void pirateDisappear(){
        for(int i = 0; i < pirates.size(); i++){
            if(pirates.get(i).getCurrentHealthPoint() <= 0){
                pirates.remove(i);
                i--;
            }
        }
    }

    /**
     * This function detects whether the sailor has win/ compelte the current level
     */
    public boolean win(){
        int LADDER_Y = 630;
        //end of the game relating constant
        int LADDER_X = 990;
        if(this.sailor.getPositionx() >= LADDER_X && this.sailor.getPositiony() >= LADDER_Y){
            //detect the timer
            //print out the end of screen message
            //else if the timer reach the maximum display time, 3 second, set the gamestate to level1
            return winMessageDisplayPeriod <= MAX_WIN_MESSAGE_DISPLAY_PERIOD;
        }
        return false;
    }

    /**
     * Getter for winMessageDisplayPerod
     */
    public int getWinMessageDisplayPeriod() {
        return winMessageDisplayPeriod;
    }

    /**
     * Setter for winMessageDisplayPerod
     */
    public void setWinMessageDisplayPeriod(int winMessageDisplayPeriod) {
        this.winMessageDisplayPeriod = winMessageDisplayPeriod;
    }

    /**
     * This function detects whether the sailor has lost the current level
     */
    public boolean lose(){
        return this.sailor.getCurrHealthPoint() <= 0;
    }

    /**
     * level1Win: print out all the win messages
     */
    public boolean level0Win(){
        //print all the start messages
        if(winMessageDisplayPeriod <= MAX_WIN_MESSAGE_DISPLAY_PERIOD){
            super.printMessage(MESSAGE_WIN_LEVEL0);
            return true;
        }
        return false;

    }
    /**
     * level1Win: print out all the lose messages
     */
    public void level0Lose(){
        //print all the start messages
        super.printMessage(MESSAGE_LOSE);
    }
}
