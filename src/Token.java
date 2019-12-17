import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class models a Token item.
 *
 * @author Harry Cassell
 * @author Ted Tay
 */
public class Token extends Item {
    private final String filePath = "assets/images/Token.png";
    private Image artAsset;
    private Point2D position;
    private boolean pickedUp;

    /**
     * Constructor.
     *
     * @param position starting position of the token.
     */
    public Token(Point2D position) {
        this.position = position;
        this.pickedUp = false;

        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Picks up the item.
     */
    @Override
    public void pickUp() {
        this.pickedUp = true;
    }


    /**
     * Returns the Image representation of this class.
     *
     * @return Image object containing an art asset.
     */
    @Override
    public Image getImage() {
        if (artAsset == null) {
            return null;
        }
        return artAsset;
    }

    /**
     * Sets the position of the item.
     *
     * @param position position ot be set.
     */
    @Override
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * retrieve the position of the item.
     *
     * @return item's position.
     */
    @Override
    public Point2D getPosition() {
        return position;
    }

    /**
     * Returns whether the item has been picked up or not.
     *
     * @return True if it has, else False.
     */
    @Override
    public boolean hasBeenPickedUp() {
        return pickedUp;
    }

    /**
     * Converts an instance of this class to a String.
     *
     * @return String instance.
     */
    @Override
    public String toString() {
        return "Token " + (int) position.getY() + " " + (int) position.getX();
    }
}
