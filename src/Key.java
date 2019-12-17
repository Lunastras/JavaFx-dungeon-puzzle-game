import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This Class models up a coloured Key.
 *
 * @author Harry Cassell
 * @author Ted Tay
 */
public class Key extends Item {
    private String filePath = "assets/images/";
    private String extension = "Key.png";
    private Image artAsset;
    private Point2D position;
    private boolean pickedUp;
    private String colour;

    /**
     * Constructor.
     *
     * @param position Starting position on the map.
     * @param colour   Colour of the key.
     */
    public Key(Point2D position, String colour) {
        this.position = position;
        this.pickedUp = false;
        this.colour = colour;

        try {
            artAsset = new Image(new FileInputStream(filePath + colour + extension));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the colour of the key.
     *
     * @param colour colour to be set.
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * retrieve the colour of the key.
     *
     * @return Key colour.
     */
    public String getColour() {
        return colour;
    }

    /**
     * picks up the item.
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
        return "Key " + (int) position.getY() + " " + (int) position.getX() + " " + colour;
    }
}
