import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Handles the interaction with the Leaderboard FXML and the leaderboard class.
 *
 * @author Harry Cassell
 */
public class LeaderboardController {
    @FXML
    public ListView leaderboard_score_list;
    @FXML
    public Button leaderboard_button_back;

    /**
     * Runs on creation.
     *
     * @throws Exception if runtime error occurs on creation.
     */
    public void initialize() throws Exception {
        Leaderboard.destroyInstance();
        ArrayList<String> topScoresFormatted = new ArrayList<>();

        for (PlayerScore score : Leaderboard.getInstance().getTopScoresArray()) {
            topScoresFormatted.add(score.toLeaderboardFormat());
        }

        Collections.sort(topScoresFormatted);

        leaderboard_score_list.getItems().addAll(topScoresFormatted);

        leaderboard_button_back.setOnAction((ActionEvent event) -> {
            try {
                Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("FXML_Files/MainMenuUpdate.fxml"));
                Scene mainMenu = new Scene(mainMenuRoot, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
                Main.getCurrentStage().setScene(mainMenu);
                Main.getCurrentStage().show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
