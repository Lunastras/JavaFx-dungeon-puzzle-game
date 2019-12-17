/**
 * This Class models a player profile.
 *
 * @author Harry Cassell
 */
public class Profile {
    private static Profile instance;
    private String name;
    private int highestClearedLvl;
    private Map saveState;

    /**
     * Creates/Returns the Profile instance.
     *
     * @return instance of Profile.
     */
    public static Profile getInstance() {
        if (instance == null) {
            instance = new Profile();
        }
        return instance;
    }

    /**
     * Destroys the Profile instance.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Private constructor.
     */
    private Profile() {
    }

    /**
     * Populates variables in the Profile instance.
     *
     * @param name              name of the profile.
     * @param highestClearedLvl highest level the player has cleared.
     */
    public void populateProfile(String name, int highestClearedLvl) {
        this.name = name;
        this.highestClearedLvl = highestClearedLvl;
    }

    /**
     * Loads the last save game of the profile.
     */
    public void loadSaveState() {
        saveState = FileReader.readSaveState(this.name);
    }

    /**
     * Returns the last save of the profile.
     *
     * @return players last save.
     */
    public Map getSaveState() {
        return saveState;
    }

    /**
     * Sets the save state of the player.
     *
     * @param saveState game to be saved.
     */
    public void setSaveState(Map saveState) {
        this.saveState = saveState;
    }

    /**
     * Retrieves the name of the profile.
     *
     * @return profile name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the profile name.
     *
     * @param name name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the highest level cleared.
     *
     * @return highest cleared level number.
     */
    public int getHighestClearedLvl() {
        return highestClearedLvl;
    }

    /**
     * Sets the highest cleared level number.
     *
     * @param highestClearedLvl number of the level.
     */
    public void setHighestClearedLvl(int highestClearedLvl) {
        if (highestClearedLvl > this.highestClearedLvl) {
            this.highestClearedLvl = highestClearedLvl;
        }
    }

    /**
     * Returns data about the current profile.
     *
     * @return profile data.
     */
    @Override
    public String toString() {
        return name + "\n" + highestClearedLvl + "\n";
    }
}
