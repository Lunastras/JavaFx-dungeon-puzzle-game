import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import javafx.scene.media.AudioClip;

import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Is responsible for handling the interactions between the FXML view of the MainMenu, and the MainMenu Class.
 *
 * @author Harry Cassell
 * @author Pete Hawkins
 */
public class MainMenuController {
    public static final String MENU_CLICK_SFX = "sound/MenuClick.mp3";
    public static final String MENU_EXIT_SFX = "sound/ExitGame.mp3";
    public static final String MENU_BACKGROUND_MUSIC = "sound/Background.mp3";
    public static final String GAME_NAME = "Tunnels of the Hopeless Raven";
    private static MediaPlayer mediaPlayer; // Static to allow play between menus without repetition of the media.
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label game_name_label;
    @FXML
    private Label current_profile_header;
    @FXML
    private Text quote_of_the_day_text;
    @FXML
    private Label current_profile_text;
    @FXML
    private Label game_name_text;
    @FXML
    private Button button_new_game;
    @FXML
    private Button button_continue_game;
    @FXML
    private Button button_new_profile;
    @FXML
    private Button button_load_profile;
    @FXML
    private Button button_leaderboard;
    @FXML
    private Button button_exit_game;

    /**
     * Run on controller creation.
     *
     * @throws Exception if runtime error occurs on creation.
     */
    public void initialize() throws Exception {
        playSong(MENU_BACKGROUND_MUSIC);

        button_new_game.setOnAction(e -> {
            playAudioClip(MENU_CLICK_SFX);
            if (MainMenu.getInstance().getLoadedProfile() == null) {
                dialogueNoLoadedProfile();
                return;
            }
            try {
                Parent leaderboardRoot = FXMLLoader.load(getClass().getResource("FXML_Files/LevelSelector.fxml"));
                Scene leaderboard = new Scene(leaderboardRoot, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
                Main.getCurrentStage().setScene(leaderboard);
                Main.getCurrentStage().show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        button_continue_game.setOnAction(e -> {
            playAudioClip(MENU_CLICK_SFX);

            if (MainMenu.getInstance().getLoadedProfile() == null) {
                dialogueNoLoadedProfile();
                return;
            }

            MainMenu.getInstance().getLoadedProfile().loadSaveState();

            if(MainMenu.getInstance().getLoadedProfile().getSaveState().getCellArray().length == 0) {
                dialogueNoSaveGame();
                return;
            } else {
                continueLevel();
            }
        });

        button_exit_game.setOnAction(e -> {
            playAudioClip(MENU_EXIT_SFX);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exiting Game");
            alert.setHeaderText(null);
            alert.setContentText("See You Next Time!");
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        });

        button_load_profile.setOnAction(e -> {
            playAudioClip(MENU_CLICK_SFX);
            List<String> choices = new ArrayList<>();
            ArrayList<String> profiles = FileReader.readAllProfiles();

            if (profiles == null || profiles.size() == 0) {
                dialogueNoProfiles();
                return;
            }

            choices.addAll(profiles);

            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Load Profile");
            dialog.setHeaderText(null);
            dialog.setContentText("Profile:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(profileName -> current_profile_text.setText(profileName));
            result.ifPresent(MainMenu.getInstance()::loadProfile);
        });

        button_new_profile.setOnAction(event -> {
            playAudioClip(MENU_CLICK_SFX);

            ArrayList<String> profiles = FileReader.readAllProfiles();

            TextInputDialog dialog = new TextInputDialog("New Profile Name");
            dialog.setTitle("New Profile");
            dialog.setHeaderText(null);
            dialog.setContentText("Profile Name:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                boolean exists = validateProfile(profiles, name);
                if (exists) {
                    current_profile_text.setText(name);
                    MainMenu.getInstance().newProfile(name);
                    MainMenu.getInstance().loadProfile(name);
                }
            });
        });

        button_leaderboard.setOnAction(event -> {
            playAudioClip(MENU_CLICK_SFX);

            try {
                Parent leaderboardRoot = FXMLLoader.load(getClass().getResource("FXML_Files/Leaderboard.fxml"));
                Scene leaderboard = new Scene(leaderboardRoot, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
                Main.getCurrentStage().setScene(leaderboard);
                Main.getCurrentStage().show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (MainMenu.getInstance().getLoadedProfile() != null) {
            current_profile_text.setText(MainMenu.getInstance().getLoadedProfile().getName());
        }

        game_name_label.setText(MainMenuController.GAME_NAME);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String quote = "Error 403";
                try {
                    quote = QuoteOfTheDay.getInstance().sendMOTD();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                quote_of_the_day_text.setText("Quote: " + quote);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 30000);
    }

    /**
     * Verifies the existence of a profile.
     *
     * @param profiles Array of profiles.
     * @param name     name of a profile.
     * @return True if profile exists else False.
     */
    private boolean validateProfile(ArrayList profiles, String name) {
        if (profiles.contains(name)) {
            dialogueProfileExists();
            return false;
        } else {
            MainMenu.getInstance().newProfile(name);
            return true;
        }
    }

    /**
     * Creates a dialogue window stating that a profile already exists.
     */
    private void dialogueProfileExists() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Profile With That Name Already Exists, Please Enter A New Name.");
        alert.showAndWait();
    }

    /**
     * Creates a dialogue window stating that a profile already exists.
     */
    private void dialogueNoProfiles() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No Profiles Exist, please make some!");
        alert.showAndWait();
    }

    /**
     * Creates a dialogue window stating that the profile has no save game.
     */
    private void dialogueNoSaveGame() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No save game to load, please click new game");
        alert.showAndWait();
    }

    /**
     * Creates a dialogue window stating that no score exists.
     */
    private void dialogueNoScores() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No scores exist, please play the game!");
        alert.showAndWait();
    }

    /**
     * Creates a dialogue window stating that no profile has been loaded.
     */
    private void dialogueNoLoadedProfile() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No profile loaded, please make or load one");
        alert.showAndWait();
    }

    /**
     * Plays the specified audio file, used for long pieces of media.
     *
     * @param path path to the audio file.
     */
    private void playSong(String path) {
        if (mediaPlayer == null) {
            Media sound = new Media(new File(path).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
        }

        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
        }
    }

    /**
     * Plays the specified audio file, used for short SFX.
     *
     * @param path path to the audio file.
     */
    private void playAudioClip(String path) {
        AudioClip soundFX = new AudioClip(new File(path).toURI().toString());
        soundFX.play();
    }

    /**
     * calls for the loading of a level from an existing save state.
     */
    public void continueLevel() {
        GameMaster game = new GameMaster(900, 700, Map.getInstance());
        Main.getCurrentStage().setScene(game.getScene());
        Main.getCurrentStage().show();
    }

    /**
     * Returns global media player for background music.
     *
     * @return mediaPlayer.
     */
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
