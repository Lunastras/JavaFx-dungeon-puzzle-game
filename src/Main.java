import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    private static Stage window;
    public static final Double WINDOW_WIDTH = 600.0;
    public static final Double WINDOW_HEIGHT = 430.0;

    /**
     * Runs on scene start.
     *
     * @param primaryStage stage to display on.
     * @throws IOException Run Time if an error occurs.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXML_Files/MainMenuUpdate.fxml"));
        Font.loadFont(getClass().getResourceAsStream("UI_Resources/Fleftex_M.ttf"), 8);
        primaryStage.setTitle("Group32Game");
        primaryStage.getIcons().add(new Image("UI_Resources/icon.jpg"));
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exit");
            alert.setHeaderText(null);
            alert.setContentText("See You Next Time!");
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        });
        Main.setCurrentStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Retrieves the current stage JavaFX is using.
     *
     * @return Stage object.
     */
    public static Stage getCurrentStage() {
        return window;
    }

    /**
     * Sets a stage for JavaFX to use.
     *
     * @param stage Stage Object to be used.
     */
    public static void setCurrentStage(Stage stage) {
        window = stage;
    }
}
