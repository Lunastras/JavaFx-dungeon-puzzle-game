import java.util.ArrayList;
import java.util.Collections;

/**
 * This Class models a Leader board with the ability to rank scores on a per level basis.
 */
public class Leaderboard {
    private static Leaderboard instance;
    private ArrayList<PlayerScore> topScoresArray;
    private ArrayList<PlayerScore> playerScoresArray;

    /**
     * Retrieves singleton instance.
     *
     * @return Instance of Leaderboard class.
     */
    public static Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

    /**
     * Destroys the instance of Leaderboard.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Constructor.
     */
    private Leaderboard() {
        topScoresArray = new ArrayList<PlayerScore>();
        playerScoresArray = new ArrayList<PlayerScore>();
        loadPlayerScores();
        sortPlayerScores();
        filterTopScoresArray();
    }

    /**
     * Retrieves every score in the score file.
     */
    private void loadPlayerScores() {
        playerScoresArray = FileReader.readScores(FileManager.GLOBAL_SCORES);
    }

    /**
     * Sorts all scores by time.
     */
    private void sortPlayerScores() {
        topScoresArray = playerScoresArray;
        Collections.sort(topScoresArray);
    }

    /**
     * Fetches best 3 scores per level.
     */
    private void filterTopScoresArray() {
        ArrayList<PlayerScore> filteredArray = new ArrayList<>();

        for (PlayerScore score : topScoresArray) {
            if (filteredArray.isEmpty()) {
                filteredArray.add(score);
            }

            int mapCount = 0;

            for (PlayerScore mapScore : filteredArray) {
                if (mapScore.getMapNumber() == score.getMapNumber()) {
                    mapCount++;
                }
            }
            if (mapCount < 3 && !filteredArray.contains(score)) {
                filteredArray.add(score);
            }
        }
        topScoresArray = filteredArray;
    }

    /**
     * Returns an array containing the top 3 scores for each level.
     *
     * @return Array of Scores.
     */
    public ArrayList<PlayerScore> getTopScoresArray() {
        return topScoresArray;
    }
}
