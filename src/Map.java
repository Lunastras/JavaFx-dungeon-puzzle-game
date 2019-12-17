import java.io.File;
import java.nio.file.InvalidPathException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This Class models a Map.
 *
 * @author Harry Cassell
 * @author Jordan Betts
 */
public class Map {
    private static Map instance;
    private Cell[][] cellArray;
    private ArrayList<Item> itemArray;
    private ArrayList<Enemy> enemyArray;
    private Player player;
    private String mapNumber; //the id of the map
    private LocalTime levelTime;
    private LocalTime mapStartTime;

    /**
     * Retrieves the singleton instance of map.
     *
     * @return Singleton instance of Map.
     */
    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    /**
     * Constructor.
     */
    private Map() {
        cellArray = new Cell[0][0];
        itemArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
        player = new Player();
        mapStartTime = LocalTime.now();
    }

    /**
     * Destroys the singleton instance of Map.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Sets the Cell array of Map.
     *
     * @param cellArray cell array to be set.
     */
    public void setCellArray(Cell[][] cellArray) {
        this.cellArray = cellArray;
    }

    /**
     * Retrieves the cell array of map.
     *
     * @return cell array.
     */
    public Cell[][] getCellArray() {
        return this.cellArray;
    }

    /**
     * Retrieves the player character.
     *
     * @return Player character.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Retrieves an array containing level items.
     *
     * @return Item array.
     */
    public ArrayList<Item> getItemArray() {
        return itemArray;
    }

    /**
     * Retrieves an array containing all level enemies.
     *
     * @return Enemy array.
     */
    public ArrayList<Enemy> getEnemyArray() {
        return enemyArray;
    }

    /**
     * Retrieves the name of the map.
     *
     * @return name of the map.
     */
    public String getMapName() {
        return ("map" + mapNumber);
    }

    /**
     * Sets the name of the map.
     *
     * @param mapName name to be set.
     */
    public void setMapName(String mapName) {
        this.mapNumber = mapName;
    }

    /**
     * Sets the time spent in the level.
     *
     * @param levelTime time to be set.
     */
    public void setLevelTime(LocalTime levelTime) {
        this.levelTime = levelTime;
    }

    /**
     * Calculates the time spent in the level.
     *
     * @return Time taken.
     */
    private String getTimeDifference() {
        LocalTime mapFinishTime = LocalTime.now();
        LocalTime timeDifference = mapFinishTime.minusHours(mapStartTime.getHour()).minusMinutes(mapStartTime.getMinute())
                .minusSeconds(mapStartTime.getSecond()).minusNanos(mapStartTime.getNano());
        return timeDifference.toString().replace(".", ":");
    }

    /**
     * Returns the name of the next map.
     *
     * @return The name of the map if it exists, otherwise it will return Null.
     */
    public String getNextMap() {
        //increments 1 at the end of the map file to get the next map.
        int a = Integer.parseInt(mapNumber);
        String name = "map" + ++a;

        try {
            String path = FileManager.MAP_FILE_PATH + name + FileManager.FILE_EXTENSION;
            File nextMap = new File(path);
            if (nextMap.exists()) {
                return name;
            } else {
                return null;
            }
        } catch (InvalidPathException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates profiles highest level cleared.
     */
    public void updateProfile() {
        Profile profile = MainMenu.getInstance().getLoadedProfile();
        profile.setHighestClearedLvl(Integer.parseInt(this.mapNumber));
        FileWriter.writeProfile(profile);
    }

    /**
     * Saves the players score.
     */
    public void saveScore() {
        String mapNumber = this.mapNumber + "\n";
        String playerName = MainMenu.getInstance().getLoadedProfile().getName() + "\n";
        String time = getTimeDifference() + "\n";
        String score = mapNumber + playerName + time;
        FileWriter.writeScore(FileManager.GLOBAL_SCORES, score);
    }

    /**
     * Saves the level.
     */
    public void saveLevel() {
        String profileName = MainMenu.getInstance().getLoadedProfile().getName();
        String mapData = Map.getInstance().toString();
        FileWriter.writeSaveState(profileName, mapData);
    }

    /**
     * Converts the contents of the map class to level string.
     *
     * @return String contents of the Map instance.
     */
    @Override
    public String toString() {
        if (cellArray == null || cellArray.length == 0) {
            return "Cell Array Empty";
        }

        StringBuilder data = new StringBuilder();
        StringBuilder cellData = new StringBuilder();
        data.append(mapNumber).append("\n");

        int height = cellArray.length;
        int width = cellArray[0].length;

        data.append(height).append(" ").append(width).append("\n");

        for (int i = 0; i < cellArray.length; i++) { // Prints cell contents of Map.
            for (int j = 0; j < cellArray[i].length; j++) {
                String cell = cellArray[i][j].getSymbol();
                if (cell.equals("D")) {
                    TokenDoor tokenDoor = (TokenDoor) cellArray[i][j];
                    cellData.append(tokenDoor.toString());
                    cellData.append("\n");
                } else if (cell.equals("T")) {
                    Teleporter teleporter = (Teleporter) cellArray[i][j];
                    cellData.append(teleporter.toString());
                    cellData.append("\n");
                } else if (cell.equals("K")) {
                    KeyDoor keyDoor = (KeyDoor) cellArray[i][j];
                    cellData.append(keyDoor.toString());
                    cellData.append("\n");
                }
                data.append(cell).append(" ");
            }
            data.append("\n");
        }

        data.append(cellData.toString());
        data.append(player.toString());
        data.append("\n");
        data.append(player.getInventoryContents());

        for (Item item : itemArray) {
            data.append(item.toString());
            data.append("\n");
        }

        for (Enemy enemy : enemyArray) {
            data.append(enemy.toString());
            data.append("\n");
        }
        data.append("LevelTime ").append(getTimeDifference());
        return data.toString();
    }
}
