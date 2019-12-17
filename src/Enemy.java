import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Abstract class for an enemy which will be used for other enemy types.
 *
 * @author Peter Hawkins
 * @author Harry Cassell
 */

public abstract class Enemy {
    private Point2D position; //Position of the enemy.
    private static Image artAsset;

    /**
     * Sets the positional co-ordinates of an enemy.
     *
     * @param point2D positional object to be set.
     */
    public abstract void setPosition(Point2D point2D);

    /**
     * Returns the positional co-ordinate of an enemy.
     *
     * @return positional co-ordinate of the enemy.
     */
    public abstract Point2D getPosition();

    /**
     * Calculates and returns the next position of an enemy.
     *
     * @return positional co-ordinate of the enemy.
     */
    public abstract Point2D getNextMove();

    /**
     * Updates the positional direction of the enemy.
     *
     * @param direction direction of the enemy.
     */
    public abstract void updatePosition(Point2D direction);

    /**
     * Retrieves the art asset of an enemy.
     *
     * @return art asset as a JavaFX Image.
     */
    public abstract Image getArtAsset();
}
