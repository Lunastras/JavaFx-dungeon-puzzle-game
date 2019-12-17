/**
 * This class models a MainMenu of a game, with this class you are able to manage player profiles, view player scores,
 * view the quote of the day, start and exit the game.
 *
 * @author Harry Cassell
 */
public class MainMenu {
    private static MainMenu instance;
    private Profile loadedProfile;

    /**
     * Constructor.
     */
    private MainMenu() {
    }

    /**
     * Retrieves the singleton instance of the MainMenu Class.
     *
     * @return MainMenu instance.
     */
    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    /**
     * Destroys the singleton instance of MainMenu.
     */
    public static void destroyInstances() {
        instance = null;
    }

    /**
     * Creates a new profile.
     *
     * @param name name of the profile ot be created.
     */
    public void newProfile(String name) {
        Profile profile = Profile.getInstance();
        profile.populateProfile(name, 0);
        FileWriter.writeProfile(profile);
    }

    /**
     * loads a profile.
     *
     * @param name name of the profile to be loaded.
     */
    public void loadProfile(String name) {
        Profile.destroyInstance();
        loadedProfile = FileReader.readProfile(name);
    }

    /**
     * Returns the currently loaded profile.
     *
     * @return loaded Profile.
     */
    public Profile getLoadedProfile() {
        return this.loadedProfile;
    }
}
