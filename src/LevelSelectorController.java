import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Handles interaction with level Selector FXML.
 *
 * @author Peter Hawkins
 */
public class LevelSelectorController {
    private static final String BOSS_MUSIC = "sound/BossMusic.mp3";
    @FXML
    private Button button_level1;
    @FXML
    private Button button_level2;
    @FXML
    private Button button_level3;
    @FXML
    private Button button_level4;
    @FXML
    private Button button_level5;
    @FXML
    private Button button_level_back;

    /**
     * Runs on creation.
     */
    public void initialize() {
        // Shows or hides button based on highest level player reached.
        if (MainMenu.getInstance().getLoadedProfile().getHighestClearedLvl() >= 0) {
            button_level1.setVisible(true);
        } else {
            button_level1.setVisible(false);
        }
        if (MainMenu.getInstance().getLoadedProfile().getHighestClearedLvl() >= 1) {
            button_level2.setVisible(true);
        } else {
            button_level2.setVisible(false);
        }
        if (MainMenu.getInstance().getLoadedProfile().getHighestClearedLvl() >= 2) {
            button_level3.setVisible(true);
        } else {
            button_level3.setVisible(false);
        }
        if (MainMenu.getInstance().getLoadedProfile().getHighestClearedLvl() >= 3) {
            button_level4.setVisible(true);
        } else {
            button_level4.setVisible(false);
        }
        if (MainMenu.getInstance().getLoadedProfile().getHighestClearedLvl() >= 4) {
            button_level5.setVisible(true);
        } else {
            button_level5.setVisible(false);
        }

        button_level1.setOnAction((ActionEvent e) -> {
            loadLevel("map1");
        });

        button_level2.setOnAction((ActionEvent e) -> {
            loadLevel("map2");
        });

        button_level3.setOnAction((ActionEvent e) -> {
            loadLevel("map3");
        });

        button_level4.setOnAction((ActionEvent e) -> {
            loadLevel("map4");
        });

        button_level5.setOnAction((ActionEvent e) -> {
            loadLevel("map5");
        });

        button_level_back.setOnAction((ActionEvent event) -> {
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

    /**
     * Method to load clean default level.
     *
     * @param mapName name of the map file.
     */
    public static void loadLevel(String mapName) {
        FileReader.readMap(mapName);
        Map map = Map.getInstance();
        //System.out.println(map.toString());

        GameMaster game = new GameMaster(900, 700, map);
        Main.getCurrentStage().setScene(game.getScene());
        Main.getCurrentStage().show();
    }

    /**
     * Plays music based on the map name.
     *
     * @param mapName name of the map.
     */
    private static void playLevelMusic(String mapName) {
        if (mapName.equals("map5")) {
            MediaPlayer mediaPlayer = MainMenuController.getMediaPlayer();
            mediaPlayer.stop();

            Media sound = new Media(new File(BOSS_MUSIC).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }
}
