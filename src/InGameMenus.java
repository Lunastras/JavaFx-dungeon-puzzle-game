import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class for creating in game menus.
 *
 * @author Radu Bucurescu
 */
public class InGameMenus {
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private AnchorPane pauseMenu;
    private AnchorPane deathMenu;
    private AnchorPane goalMenu;
    private MapRenderer renderer;

    /**
     * Constructs an InGameMenu.
     *
     * @param WINDOW_WIDTH  width of the window.
     * @param WINDOW_HEIGHT height of the window.
     * @param renderer      object to render.
     */
    public InGameMenus(final int WINDOW_WIDTH, final int WINDOW_HEIGHT, MapRenderer renderer) {
        this.WINDOW_HEIGHT = WINDOW_HEIGHT;
        this.WINDOW_WIDTH = WINDOW_WIDTH;
        pauseMenu = new AnchorPane();
        deathMenu = new AnchorPane();
        goalMenu = new AnchorPane();
        this.renderer = renderer;
        createPauseMenu();
        createDeathMenu();
        createGoalMenu();
    }

    /**
     * Get the pause screen menu.
     *
     * @return the pauseMenu
     */
    public AnchorPane getPauseMenu() {
        return pauseMenu;
    }

    /**
     * Get the death screen menu.
     *
     * @return the deathMenu
     */
    public AnchorPane getDeathMenu() {
        return deathMenu;
    }

    /**
     * Get the Level complete menu.
     *
     * @return the goalMenu
     */
    public AnchorPane getGoalMenu() {
        return goalMenu;
    }

    /**
     * Creates a button.
     *
     * @param text displayed on the button.
     * @return the button object after creation.
     */
    private Button createButton(String text) {
        Button button = new Button(text);

        button.setStyle("-fx-border-color: #e2e2e2; -fx-border-width: 2px; -fx-border-radius: 8px;");
        button.setStyle("-fx-background-radius: 8px;\n"
                + "-fx-background-color: #2B1C2D;\n"
                + "-fx-font-family: 'Fleftex';\n"
                + "-fx-font-size: 15pt;\n"
                + "-fx-text-fill: #ffffff;\n"
                + "-fx-pref-width: 180;\n"
                + "-fx-pref-height: 50;");
        return button;
    }

    /**
     * Creates the Pause Menu.
     */
    private void createPauseMenu() {
        VBox list = new VBox();

        Text pause = new Text("Paused");

        Button resume = createButton("Resume");
        Button exit = createButton("Main Menu");
        Button restart = createButton("Restart");
        Button save = createButton("Save Level");

        resume.setOnAction((ActionEvent e) -> {
            resumeButton();
        });

        exit.setOnAction((ActionEvent e) -> {
            exitButton();
        });

        restart.setOnAction((ActionEvent e) -> {
            restartButton();
        });

        save.setOnAction((ActionEvent e) -> {
            saveButton();
        });

        pause.setFill(Color.WHITE);
        pause.setFont(Font.font("Fleftex", 60));
        pause.setTextAlignment(TextAlignment.CENTER);

        list.getChildren().addAll(resume, restart, save, exit);

        list.setSpacing(5.0);
        list.setAlignment(Pos.CENTER);
        pauseMenu.getChildren().addAll(list, pause);

        AnchorPane.setBottomAnchor(list, (double) WINDOW_HEIGHT / 2 - 130.0);
        AnchorPane.setRightAnchor(list, (double) WINDOW_WIDTH / 2 - 85.0);

        AnchorPane.setBottomAnchor(pause, (double) WINDOW_HEIGHT / 2 + 100.0);
        AnchorPane.setRightAnchor(pause, (double) WINDOW_WIDTH / 2 - 100);
    }

    /**
     * Creates the Goal Menu.
     */
    private void createGoalMenu() {
        VBox list = new VBox();

        Text clear = new Text("Cleared!");

        Button next = createButton("Next Level");
        Button exit = createButton("Main Menu");
        Button restart = createButton("Restart");

        next.setOnAction((ActionEvent e) -> {
            nextButton();
        });

        exit.setOnAction((ActionEvent e) -> {
            exitButton();
        });

        restart.setOnAction((ActionEvent e) -> {
            restartButton();
        });

        clear.setFill(Color.WHITE);
        clear.setFont(Font.font("Fleftex", 60));
        clear.setTextAlignment(TextAlignment.CENTER);

        //Adds the option for the next level only if it exists.
        if (Map.getInstance().getNextMap() != null) {
            list.getChildren().add(next);
        }

        list.getChildren().addAll(restart, exit);
        list.setSpacing(5.0);
        list.setAlignment(Pos.CENTER);
        goalMenu.getChildren().addAll(list, clear);

        AnchorPane.setBottomAnchor(list, (double) WINDOW_HEIGHT / 2 - 130.0);
        AnchorPane.setRightAnchor(list, (double) WINDOW_WIDTH / 2 - 85.0);

        AnchorPane.setBottomAnchor(clear, (double) WINDOW_HEIGHT / 2 + 100.0);
        AnchorPane.setRightAnchor(clear, (double) WINDOW_WIDTH / 2 - 130);
    }

    /**
     * Button to load next level.
     */
    private void nextButton() {
        LevelSelectorController.loadLevel(Map.getInstance().getNextMap());
    }

    /**
     * Button to resume the game.
     */
    private void resumeButton() {
        renderer.unPause();
    }

    /**
     * Button to exit to main menu.
     */
    private void exitButton() {
        try {
            Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("FXML_Files/MainMenuUpdate.fxml"));
            Scene mainMenu = new Scene(mainMenuRoot, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
            Main.getCurrentStage().setScene(mainMenu);
            Main.getCurrentStage().show();
        } catch (Exception e) {
            System.out.println("ERROR TRYING TO LOAD MENU");
        }
    }

    /**
     * Button to restart the level.
     */
    private void restartButton() {
        LevelSelectorController.loadLevel(Map.getInstance().getMapName());
    }

    /**
     * Button to save the game.
     */
    private void saveButton() {
        System.out.println(Map.getInstance().toString());
        Map.getInstance().saveLevel();
    }

    /**
     * Create death menu screen.
     */
    private void createDeathMenu() {
        VBox list = new VBox();

        Button exit = createButton("Main Menu");
        Button restart = createButton("Restart");
        Text gameOver = new Text("GameOver");

        gameOver.setFill(Color.WHITE);
        gameOver.setFont(Font.font("Fleftex", 60));
        gameOver.setTextAlignment(TextAlignment.CENTER);

        exit.setOnAction((ActionEvent e) -> {
            exitButton();
        });

        restart.setOnAction((ActionEvent e) -> {
            restartButton();
        });

        list.getChildren().addAll(restart, exit);
        list.setSpacing(5.0);
        list.setAlignment(Pos.CENTER);

        deathMenu.getChildren().addAll(list, gameOver);

        AnchorPane.setBottomAnchor(list, (double) WINDOW_HEIGHT / 2 - 100.0);
        AnchorPane.setRightAnchor(list, (double) WINDOW_WIDTH / 2 - 85.0);

        AnchorPane.setBottomAnchor(gameOver, (double) WINDOW_HEIGHT / 2 + 100.0);
        AnchorPane.setRightAnchor(gameOver, (double) WINDOW_WIDTH / 2 - 155);
    }
}
