import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * This Class models a Generic Item.
 *
 * @author Harry Cassell
 * @author Ted Tay
 */
public abstract class Item {
    private Point2D position;
    private boolean pickedUp = false;

    /**
     * Method to pick up the item.
     */
    public abstract void pickUp();

    /**
     * Retrieves teh art asset of the item.
     *
     * @return JavaFX Image of the art asset.
     */
    public abstract Image getImage();

    /**
     * Sets teh position of the item on the map.
     *
     * @param position position to be set.
     */
    public abstract void setPosition(Point2D position);

    /**
     * Retrieves the items position on the map.
     *
     * @return Position of the item.
     */
    public abstract Point2D getPosition();

    /**
     * Checks whether the items has been picked up or not.
     *
     * @return True if picked up, else False.
     */
    public abstract boolean hasBeenPickedUp();

}
