import bagel.*;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This class is used to handle all the Level1 relating actions
 */
public class Level1 extends Level{

    //readcsv file relating constants (specify which column records the data of what)
    private final static String INITIAL_POSITIONS = "res/level1.csv";
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
    private final static String BLACKBEARD = "Blackbeard";
    private final static String BLOCK = "Block";
    private final static String TREASURE = "Treasure";
    private final static String POTION = "Potion";
    private final static String SWORD = "Sword";
    private final static String ELIXIR = "Elixir";

    //used to convert attributes relating to pirate to blackbeard
    private final static int BLACKBEARD_MULTIPLIER = 2;

    // initiation of instance of sailor and arraylist of pirates, blocks
    private Sailor sailor;
    private Pirate blackbeard;
    private Treasure treasure;
    private final ArrayList<Pirate> pirates = new ArrayList<>(); // array list that stores all the pirates' position
    private final ArrayList<Bomb> bombs = new ArrayList<>(); // array list that stores all the blocks' position
    private final ArrayList<Projectile> projectiles = new ArrayList<>(); // array list that stores all the projectiles' position

    private final ArrayList<Inventory> inventories = new ArrayList<>();

    //messages and its specific locations to print on screen
    private final static String MESSAGE_START = "PRESS SPACE TO START";
    private final static String MESSAGE_ATTACK = "PRESS S TO ATTACK";
    private final static String MESSAGE_WIN_CONDITION_LEVEL1 = "FIND THE TREASURE";
    private final static String MESSAGE_WIN_LEVEL1 = "CONGRATULATIONS!";
    private final static String MESSAGE_LOSE = "GAME OVER";
    private final static int DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE = 402;
    private final static int DEFAULT_SPACE_BETWEEN_MESSAGES = 70;

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
    public Level1(){
        //read the csv file to initialise all the blocks and the sailor
        readcsv(INITIAL_POSITIONS);
    }

    /**
     * level0Menu: print out all the start messages
     */
    public void level1Menu(){
        //print all the start messages
        super.printMessage(MESSAGE_START, DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE
                - DEFAULT_SPACE_BETWEEN_MESSAGES);
        super.printMessage(MESSAGE_ATTACK);
        super.printMessage(MESSAGE_WIN_CONDITION_LEVEL1, DEFAULT_Y_COORDINATE_OF_ALL_BOTTOM_LEFT_MESSAGE
                + DEFAULT_SPACE_BETWEEN_MESSAGES);
    }

    /**
     * level0Running: perform all the actions required during the running state of the game
     */
    public String level1Running(Input input){
        //sailor's action handling:

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
            sailorCollideWithBlackbeardDetect();
        }

        //if the sailorState is cooldown, update the cooldownPeriod
        if(this.sailor.getSailorState().equals("COOLDOWN")){
            this.sailor.setCooldownPeriod(this.sailor.getCooldownPeriod() + 1);
        }

        //update the sailor's position based on the input entered by user
        this.sailor.move();

        //only draw the sailor if the updated position is not out of bound
        if(OutOfBound(sailor.getPositionx(), sailor.getPositiony(), minX, maxX, minY, maxY)){
            this.sailor.bounceBack();
        }
        this.sailor.render();


        //pirates' action handling

        //only render the blackbeard if its healthpoint is above 0
        if(blackbeard.getCurrentHealthPoint() > 0){
            //blackbeard handling
            this.blackbeard.move();
            //if the updated position is out of bound, bounce back before render
            if(super.OutOfBound(this.blackbeard.getPositionx(), this.blackbeard.getPositiony(), minX, maxX, minY, maxY)) {
                this.blackbeard.changeCurrDirection();
                //this.blackbeard.render();
            }

            this.blackbeard.render();
            this.blackbeard.drawHealthPoint();

            //blackbeard's attack logic handling
            this.blackbeard.attack();
            //System.out.println(this.blackbeard.isDamagble());
            if(this.blackbeard.getAttackState().equals(READY_TO_ATTACK)){
                //if the sailor is inside its attackrange, pirate will fire one projectile and change state to COOLDOWN
                sailorWithinPirateAttackRange(this.blackbeard, true);
            }
            //if the pirate's current state is in COOLDOWN, update the CooldownPeriod timer
            if(this.blackbeard.getAttackState().equals(COOLDOWN)){
                this.blackbeard.setCooldownPeriod(this.blackbeard.getCooldownPeriod() + 1);
            }
            // if the pirate's current state is in INVINCIBLE, update the InvinciblePeriod timer
            if(!this.blackbeard.isDamagble()){
                this.blackbeard.invincible();
                this.blackbeard.setInvinciblePeriod(this.blackbeard.getInvinciblePeriod() + 1);
            }
        }



        //only draw the pirate if its position is not out of bound
        for (Pirate value : pirates) {
            value.move();
            //if the updated position is out of bound, bounce back before render
            if(super.OutOfBound(value.getPositionx(), value.getPositiony(), minX, maxX, minY, maxY)) {
                value.changeCurrDirection();
            }
            value.render();
            value.drawHealthPoint();
        }
        //remove the pirate from the screen if its healthpoint is below or equals to 0
        pirateDisappear();

        // it the pirate's state is READY_TO_ATTACK, fire projectile if the sailor is inside its attackrange
        for(Pirate value: pirates){
            value.attack();
            if(value.getAttackState().equals(READY_TO_ATTACK)){
                //if the sailor is inside its attackrange, pirate will fire one projectile and change state to COOLDOWN
                sailorWithinPirateAttackRange(value, false);
            }
            //if the pirate's current state is in COOLDOWN, update the CooldownPeriod timer
            if(value.getAttackState().equals(COOLDOWN)){
                value.setCooldownPeriod(value.getCooldownPeriod() + 1);
            }
            // if the pirate's current state is in INVINCIBLE, update the InvinciblePeriod timer
            if(!value.isDamagble()){
                value.invincible();
                value.setInvinciblePeriod(value.getInvinciblePeriod() + 1);

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
        for (Bomb value : bombs) {
            value.render();
            //update the renderPeriod if collision between the sailor and the bomb has occurred
            if(value.getState().equals("exploision")){
                value.setRenderPeriod(value.getRenderPeriod() + 1);
            }
        }

        //detect the collision between blocks and sailor or pirate, and perform subsequent action
        sailorCollideWithBomb();
        pirateCollideWithBomb();
        blackbeardCollideWithBomb();

        //render all the stationary
        this.treasure.render();

        //go through the inventory list and if it collides with the sailor, apply the corresponding effect on it
        for(int i = 0; i < inventories.size(); i++){
            inventories.get(i).render();
            if (sailor.createRectangle(sailor.getCurrImage()).intersects(inventories.get(i).createRectangle())){
                this.sailor.addIcons(inventories.get(i).pickUpEffect(sailor));
                inventories.remove(i);
                i--;
            }
        }

        //winning and lose condition detection
        if(win()) {
            return WIN;
        } else if(lose()){
            return LOSE;
        }
        return "";
    }


    /**
     * This function detects collision via rectangle class, if detected drop the health point
     */
    public void sailorCollideWithBomb(){
        for (int i = 0; i < bombs.size(); i++) {
            //if there's a collision between the sailor and the bomb
            if (sailor.createRectangle(sailor.getCurrImage()).intersects(bombs.get(i).createRectangle())) {
                sailor.bounceBack();
                //the bomb can only attack the sailor if its in attack stage
                if(bombs.get(i).getState().equals("attack")){
                    sailor.damage(bombs.get(i).getDAMAGE());
                    //log messages
                    String LOG_BOMB_TO_SAILOR = "Bomb inflicts %d damage points on Sailor. Sailor's current health %d/%d\n";
                    System.out.printf(LOG_BOMB_TO_SAILOR, bombs.get(i).getDAMAGE(),sailor.getCurrHealthPoint(),
                            sailor.getMaxHealthPoint());
                    bombs.get(i).setState("explosion");
                    bombs.get(i).attack();
                }
            }

            //counting the display period of the bomb
            if(bombs.get(i).getState().equals("explosion")){
                bombs.get(i).setRenderPeriod(bombs.get(i).getRenderPeriod() + 1);
                //System.out.printf("renderperiod is %d\n", bombs.get(i).getRenderPriod());
                //once the bomb has been displayed for long enough, remove it from the array list and stop render it
                if(bombs.get(i).attack()){
                    bombs.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * This function detects collision via rectangle class, if detected drop the health point and bounce back
     */
    public void blackbeardCollideWithBomb(){
        for (Bomb block : bombs) {
            //if there's a collision, the sailor bounces back to the position before collision
            if (blackbeard.createRectangle(blackbeard.getCurrImage()).intersects(block.createRectangle())) {
                blackbeard.changeCurrDirection();
                break;
            }
        }
    }

    /**
     * This function detects collision via rectangle class, if detected drop the health point and bounce back
     */
    public void pirateCollideWithBomb(){
        for (Bomb block : bombs) {
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
            //if collision is found, return true
            if (value.createRectangle(value.getCurrImage()).intersects(sailor.createRectangle(sailor.getCurrImage()))
                    && value.isDamagble()) {
                value.damage(sailor.getCurrDamage());
                //print out the log message
                String LOG_SAILOR_TO_PIRATE = "Sailor inflict %d damage points on Pirate. Pirate's current health: %d/%d\n";
                System.out.printf(LOG_SAILOR_TO_PIRATE, sailor.getCurrDamage(),
                        value.getCurrentHealthPoint(), value.getMaxHealthPoint());
                value.setDamagble(false);
                //System.out.printf("the pirate's current health point is %d\n", value.getCurrentHealthPoint());
            }
        }
    }

    /**
     * This function detects the attack range between the sailor and the blackbeard
     */
    public void sailorCollideWithBlackbeardDetect(){
        //if collision is found, blackbeard inflict the damage and switch its state to not damageable/ invincible
        if (this.blackbeard.createRectangle(this.blackbeard.getCurrImage())
                .intersects(sailor.createRectangle(sailor.getCurrImage())) && this.blackbeard.isDamagble()){
            this.blackbeard.damage(sailor.getCurrDamage());
            this.blackbeard.setDamagble(false);
        }
    }


    /**
     * This function detects whether the sailor is within pirate's attach range
     */
    public void sailorWithinPirateAttackRange(Pirate value, boolean isBlackbeard){
        //detects whether the sailor is within pirate's attach range
        if (value.createAttackRangeRectangle(value.getCurrImage())
                .intersects(sailor.createRectangle(sailor.getCurrImage()))) {

            //if the sailor is within pirate's attach range, add a projectile (based on the pirate type) to projectiles
            if(isBlackbeard){
                projectiles.add(new Projectile(value.getPositionx(), value.getPositiony(),
                        this.sailor.getPositionx(), this.sailor.getPositiony(), true));
            } else {
                projectiles.add(new Projectile(value.getPositionx(), value.getPositiony(),
                        this.sailor.getPositionx(), this.sailor.getPositiony(), false));
            }
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
                //print out the log message based on the pirate type
                //Projectile's damage point is used to determine whether the pirate or the blackbeard fire it
                int BLACKBEARD_DAMAGE = 20;
                if(projectiles.get(i).getCurrDamage() == BLACKBEARD_DAMAGE){
                    String LOG_BLACKBEARD_TO_SAILOR = "Blackbeard inflicts %d damage points on Sailor. Sailor's current health: %d/%d\n";
                    System.out.printf(LOG_BLACKBEARD_TO_SAILOR,
                            projectiles.get(i).getCurrDamage(), sailor.getCurrHealthPoint(), sailor.getMaxHealthPoint());
                } else {
                    String LOG_PIRATE_TO_SAILOR = "Pirate inflicts %d damage points on Sailor. Sailor's current health: %d/%d\n";
                    System.out.printf(LOG_PIRATE_TO_SAILOR,
                            projectiles.get(i).getCurrDamage(), sailor.getCurrHealthPoint(), sailor.getMaxHealthPoint());
                }
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
                String[] cells = text.split(",");
                String name = cells[NAME_COL_NUM];
                int x_intercept = Integer.parseInt(cells[X_COORDINATE_COL_NUM]);
                int y_intercept = Integer.parseInt(cells[Y_COORDINATE_COL_NUM]);
                //System.out.format("%s need to be at %d, %d\n", name, x_intercept, y_intercept);

                //accept the level boundary from the csv file
                if(name.equals(BOUNDARY_TOP_LEFT)){
                    this.minX = x_intercept;
                    this.minY = y_intercept;
                }
                if(name.equals(BOUNDARY_BOTTOM_RIGHT)){
                    this.maxX = x_intercept;
                    this.maxY = y_intercept;
                }

                //create a sailor based on the coordinates in world file
                if(name.equals(SAILOR)){
                    this.sailor = new Sailor(x_intercept, y_intercept);
                }

                //create a treasure based on the coordinates in world file
                if(name.equals(TREASURE)){
                    this.treasure = new Treasure(x_intercept, y_intercept);
                }

                //create a blackbeard based on the coordinates in world file
                if(name.equals(BLACKBEARD)){
                    this.blackbeard = new Pirate(x_intercept, y_intercept,
                        super.getRandSpeed() * BLACKBEARD_MULTIPLIER, super.getRandDirec(), true);
                }

                //create and add the pirates to the pirates arraylist based on the coordinates in world file
                if(name.equals(PIRATE)){
                    pirates.add(new Pirate(x_intercept, y_intercept, super.getRandSpeed(), super.getRandDirec(), false));
                }

                //create and add the bombs to the bombs arraylist based on the coordinates in world file
                if(name.equals(BLOCK)){
                    bombs.add(new Bomb(x_intercept, y_intercept));
                }

                //create and add the inventory to the inventories arraylist
                if(name.equals(SWORD)){
                    inventories.add(new Sword(x_intercept,y_intercept));
                }
                if(name.equals(POTION)){
                    inventories.add(new Potion(x_intercept,y_intercept));
                }
                if(name.equals(ELIXIR)){
                    inventories.add(new Elixir(x_intercept,y_intercept));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * this function is responsible to handle the pirate disappearance from the screen after its healthpoint drop to 0
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
     * This function detect whether the sailor has won/ completed the current level
     * For level1, the sailor won if it collide with the treasure
     */
    public boolean win(){
        return (sailor.createRectangle(sailor.getCurrImage()).intersects(this.treasure.createRectangle()));
    }


    /**
     * This function detect whether the sailor has lost the current level/ healthpoint drops to 0
     */
    public boolean lose(){
        return this.sailor.getCurrHealthPoint() <= 0;
    }

    /**
     * level1Win: display the congratulation message
     */
    public void level1Win(){
        super.printMessage(MESSAGE_WIN_LEVEL1);
    }
    /**
     * level1Win: display the gameover messages
     */
    public void level1Lose(){
        super.printMessage(MESSAGE_LOSE);
    }




}
