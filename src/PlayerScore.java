import java.time.LocalTime;

/**
 * This class represents a level score.
 *
 * @author Harry Cassell
 */
public class PlayerScore implements Comparable {
    private int mapNumber;
    private String playerName;
    private LocalTime time;

    /**
     * Constructor.
     *
     * @param mapNumber  number of the map the score was achieved on.
     * @param playerName name of the player who achieved the score.
     * @param time       time takes to complete the level.
     */
    public PlayerScore(int mapNumber, String playerName, LocalTime time) {
        this.mapNumber = mapNumber;
        this.playerName = playerName;
        this.time = LocalTime.of(0, time.getMinute(), time.getSecond(), time.getNano());
    }

    /**
     * Retrieves map number.
     *
     * @return number of the map.
     */
    public int getMapNumber() {
        return mapNumber;
    }

    /**
     * Sets map number.
     *
     * @param mapNumber number of the map.
     */
    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
    }

    /**
     * Retrieves players name.
     *
     * @return name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets players name.
     *
     * @param playerName name of a player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * retrieves the time taken on a map.
     *
     * @return time taken.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets time taken on a map.
     *
     * @param time time to be set.
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Convert contents of the class to string format.
     *
     * @return player score.
     */
    @Override
    public String toString() {
        return mapNumber + "\n" + playerName + "\n" + time.toString() + "\n";
    }

    /**
     * Formats and returns a player score to be used with teh leaderboard class.
     *
     * @return formatted score string.
     */
    public String toLeaderboardFormat() {
        return mapNumber + " | " + playerName + " | " + time.toString();
    }

    /**
     * Compares two player scores.
     *
     * @param o score to compare against.
     * @return integer representing if the time was faster or slower than the comparison time.
     */
    @Override
    public int compareTo(Object o) {
        LocalTime comparisonTime = ((PlayerScore) o).getTime();
        return this.time.compareTo(comparisonTime);
    }
}
