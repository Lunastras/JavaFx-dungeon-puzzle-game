import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Class used for enemy which targets the player, but not challenging.
 *
 * @author Peter Hawkins
 * @author Radu Bucurescu
 */
public class DumbTargetingEnemy extends Enemy {
    private Point2D position; //Position of enemy.
    private Cell[][] currentMapState = Map.getInstance().getCellArray(); // MapState
    private String filePath = "assets/images/DumbTargetingEnemy.png";
    private static Image artAsset;

    /**
     * Construct a new DumbTargetingEnemy().
     *
     * @param point2D value to set a default point2D object.
     */
    public DumbTargetingEnemy(Point2D point2D) {
        this.position = point2D;
        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to calculate enemies next move relative to the board state.
     *
     * @return enemies new position on the board.
     */
    @Override
    public Point2D getNextMove() {
        int currentX = (int) position.getX();
        int currentY = (int) position.getY();
        int playerX = (int) Map.getInstance().getPlayer().getPosition().getX();
        int playerY = (int) Map.getInstance().getPlayer().getPosition().getY();

        //The direction the enemy has to move with.
        int movementX = 0;
        int movementY = 0;

        if (currentX > playerX) {
            movementX = -1;
        } else if (currentX < playerX) {
            movementX = 1;
        }
        if (currentY > playerY) {
            movementY = -1;
        } else if (currentY < playerY) {
            movementY = 1;
        }

        if (currentMapState[currentY + movementY][currentX].getClass() == Ground.class && movementY != 0) {
            return new Point2D(0, movementY);
        } else if (currentMapState[currentY][currentX + movementX].getClass() == Ground.class && movementX != 0) {
            return new Point2D(movementX, 0);
        }
        return new Point2D(0, 0);
    }

    /**
     * Sets the Point2D object/x and y of StraightLineEnemy.
     *
     * @param position value to set a default point2D object.
     */
    @Override
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Method to retrieve point2D object.
     *
     * @return the position of enemy.
     */
    @Override
    public Point2D getPosition() {
        return position;
    }

    /**
     * Updates the position of enemy in the renderer.
     *
     * @param pos x and y coordinate to add to position.
     */
    @Override
    public void updatePosition(Point2D pos) {
        this.position = new Point2D(position.getX() + pos.getX(), position.getY() + pos.getY());
    }

    /**
     * Gets the image for Smart enemy.
     *
     * @return the image for smart enemy.
     */
    @Override
    public Image getArtAsset() {
        if (artAsset == null) {
            return null;
        }
        return artAsset;
    }

    /**
     * String representation of smart enemy x and y position.
     *
     * @return string of enemies x and y position.
     */
    @Override
    public String toString() {
        return "Enemy " + (int) position.getX() + " " + (int) position.getY() + " DT";
    }
}
