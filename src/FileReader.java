import javafx.geometry.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This Class models a File Reader, with this class you can read the contents of files.
 *
 * @author Harry Cassell
 */
public class FileReader extends FileManager {

    /**
     * Read the contents of a profile.
     *
     * @param profileName name of the profile.
     * @return a Profile.
     */
    public static Profile readProfile(String profileName) {
        Profile.destroyInstance();
        String name = null;
        int highestLvl = -1;
        String path = PROFILE_FILE_PATH + profileName + FILE_EXTENSION;
        File profileFile = new File(path);

        try {
            Scanner in = new Scanner(profileFile);
            name = in.nextLine();
            highestLvl = in.nextInt();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (name == null || highestLvl == -1) {
            return null;
        } else {
            Profile.getInstance().populateProfile(name, highestLvl);
            return Profile.getInstance();
        }
    }

    /**
     * Reads the names of all currently existing profiles.
     *
     * @return Names of all existing profiles.
     */
    public static ArrayList<String> readAllProfiles() {
        ArrayList<String> profiles = new ArrayList<>();
        File profileFolder = new File(FileReader.PROFILE_FILE_PATH);
        File[] files = profileFolder.listFiles();

        if (files == null) {
            return null;
        } else {
            for (File profile : files) {
                profiles.add(profile.getName().replace(".txt", ""));
            }
        }
        return profiles;
    }

    /**
     * Read the contents of a save-state.
     *
     * @param profileName name of the profile.
     * @return contents of the save-state.
     */
    public static Map readSaveState(String profileName) {
        Map.destroyInstance();
        String path = SAVE_FILE_PATH + profileName + SAVE_STATE + FILE_EXTENSION;
        File saveState = new File(path);
        readMapFile(saveState);
        return Map.getInstance();
    }

    /**
     * Read the contents of a score file.
     *
     * @param scoreFileName name of the score file.
     * @return contents of the score file.
     */
    public static ArrayList<PlayerScore> readScores(String scoreFileName) {
        ArrayList<PlayerScore> playerScores = new ArrayList<>();
        String path = SCORE_FILE_PATH + GLOBAL_SCORES + FILE_EXTENSION;
        File scores = new File(path);

        try {
            Scanner in = new Scanner(scores);
            while (in.hasNextLine()) {
                int mapNo = in.nextInt();
                in.nextLine();
                String name = in.nextLine();
                String stringTime = in.nextLine();
                PlayerScore score = new PlayerScore(mapNo, name, stringToLocalTime(stringTime));
                playerScores.add(score);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return playerScores;
    }

    /**
     * Converts a String in the format HH:MM:SS:NN to a time object.
     *
     * @param timeString String to be converted.
     * @return Created time object.
     */
    private static LocalTime stringToLocalTime(String timeString) {
        String[] splitTime = timeString.split(":");
        return LocalTime.of(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]), Integer.parseInt(splitTime[2]), Integer.parseInt(splitTime[3]));
    }

    /**
     * Read the contents of a map file.
     *
     * @param mapName name of the map file.
     * @return contents of the map file.
     */
    public static Map readMap(String mapName) {
        Map.destroyInstance();
        String path = MAP_FILE_PATH + mapName + FILE_EXTENSION;
        File map = new File(path);
        readMapFile(map);
        return Map.getInstance();
    }

    /**
     * Reads the contents of a map file, setting up the map in the process.
     *
     * @param mapFile Map to be read.
     */
    private static void readMapFile(File mapFile) {
        Map map = Map.getInstance();
        try {
            Scanner in = new Scanner(mapFile);
            String mapName = in.nextLine();
            map.setMapName(mapName);
            int height = in.nextInt();
            int width = in.nextInt();
            map.setCellArray(new Cell[height][width]);
            in.nextLine();

            for (int i = 0; i < map.getCellArray().length; i++) {
                String line = in.nextLine();
                if (!line.equals("")) {
                    String[] cells = line.split(" ");
                    for (int j = 0; j < cells.length; j++) {
                        Map.getInstance().getCellArray()[i][j] = createCell(cells[j].charAt(0));
                    }
                }
            }

            while (in.hasNextLine()) { // Attributes for item/cells
                String line = in.nextLine();
                String[] attributes = line.split(" ");

                if (attributes[0].equals("Inventory")) {
                    setInventory(attributes);
                } else if (attributes[0].equals("LevelTime")) {
                    setLevelTime(attributes);
                } else {
                    setAttributes(attributes);
                }
            }
            in.close();
            Profile.getInstance().setSaveState(Map.getInstance());
        } catch (FileNotFoundException e) {
            Profile.getInstance().setSaveState(null);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a Cell object from a given symbol.
     *
     * @param cell Character to be turned into a cell.
     * @return Created Cell object.
     */
    private static Cell createCell(char cell) {
        switch (cell) {
            case '_':
                return new Ground();
            case 'F':
                return new Fire();
            case 'W':
                return new Water();
            case 'T':
                return new Teleporter();
            case 'G':
                return new Goal();
            case 'K':
                return new KeyDoor();
            case 'D':
                return new TokenDoor();
            default:
                return new Wall();
        }
    }

    /**
     * Applies parsed attributes onto the map class.
     *
     * @param attributes array of attributes to be set.
     */
    private static void setAttributes(String[] attributes) {
        String object = attributes[0];

        int y = Integer.parseInt(attributes[1]);
        int x = Integer.parseInt(attributes[2]);

        switch (object) {
            case "Token":
                Map.getInstance().getItemArray().add(new Token(new Point2D(x, y)));
                return;
            case "Player":
                Map.getInstance().getPlayer().setPosition(new Point2D(x, y));
                return;
            case "Enemy":
                String type = attributes[3];
                if (type.equals("SL")) {
                    String direction = attributes[4];
                    Map.getInstance().getEnemyArray().add(new StraightLineEnemy(new Point2D(x, y), direction));
                } else if (type.equals("WF")) {
                    String direction = attributes[4];
                    Map.getInstance().getEnemyArray().add(new WallFollowingEnemy(new Point2D(x, y), direction));
                } else if (type.equals("DT")) {
                    Map.getInstance().getEnemyArray().add(new DumbTargetingEnemy(new Point2D(x, y)));
                } else if (type.equals("ST")) {
                    Map.getInstance().getEnemyArray().add(new SmartTargetingEnemy(new Point2D(x, y)));
                }
                return;
            case "Key":
                String keyColour = attributes[3];
                Map.getInstance().getItemArray().add(new Key(new Point2D(x, y), keyColour));
                return;
            case "TDoor":
                int numberOfTokens = Integer.parseInt(attributes[3]);
                TokenDoor tokenDoor = (TokenDoor) Map.getInstance().getCellArray()[y][x];
                tokenDoor.setPosition(new Point2D(x, y));
                tokenDoor.setNumberOfTokens(numberOfTokens);
                return;
            case "KDoor":
                String doorColour = attributes[3];
                KeyDoor keyDoor = (KeyDoor) Map.getInstance().getCellArray()[y][x];
                keyDoor.setPosition(new Point2D(x, y));
                keyDoor.setColour(doorColour);
                return;
            case "Tele":
                int destinationY = Integer.parseInt(attributes[3]);
                int destinationX = Integer.parseInt(attributes[4]);
                Teleporter origin = (Teleporter) Map.getInstance().getCellArray()[y][x];
                Teleporter destination = (Teleporter) Map.getInstance().getCellArray()[destinationY][destinationX];
                origin.setPosition(new Point2D(x, y));
                origin.setLink(destination);
                destination.setPosition(new Point2D(destinationX, destinationY));
                destination.setLink(origin);
                return;
            case "Boots":
                String bootType = attributes[3];
                if (bootType.equals("Fire")) {
                    Map.getInstance().getItemArray().add(new FireBoots(new Point2D(x, y)));
                } else if (bootType.equals("Water")) {
                    Map.getInstance().getItemArray().add(new WaterBoots(new Point2D(x, y)));
                }
                return;
            default:
                System.out.println("Could Not Set Attributes for " + object);
                return;
        }
    }

    /**
     * Applies the parsed attributes onto the players inventory.
     *
     * @param attributes Attributes to be set.
     */
    private static void setInventory(String[] attributes) {
        String object = attributes[1];

        switch (object) {
            case "Key":
                Map.getInstance().getPlayer().getInventory().add(new Key(new Point2D(0, 0), attributes[2]));
                return;
            case "Token":
                Map.getInstance().getPlayer().getInventory().add(new Token(new Point2D(0, 0)));
                return;
            case "FireBoots":
                Map.getInstance().getPlayer().getInventory().add(new FireBoots(new Point2D(0, 0)));
                return;
            case "WaterBoots":
                Map.getInstance().getPlayer().getInventory().add(new WaterBoots(new Point2D(0, 0)));
                return;
            default:
                System.out.println("Could Not Put " + object + " In Inventory");
                return;
        }
    }

    /**
     * Sets the maps level time.
     *
     * @param time Split String of the time to be set.
     */
    private static void setLevelTime(String[] time) {
        Map.getInstance().setLevelTime(stringToLocalTime(time[1]));
    }
}
