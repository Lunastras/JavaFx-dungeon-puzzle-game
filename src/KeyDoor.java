import javafx.scene.image.Image;
import javafx.geometry.Point2D;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This Class models a door that can be opened with a key.
 *
 * @author Harry Cassell
 * @author Dimitris Savva
 */
public class KeyDoor extends Door {
    private String filePath = "assets/images/";
    private String extension = "KeyDoor.png";
    private Image artAsset;
    private final String symbol = "K";
    private Point2D position;
    private boolean isPassable;
    private String colour;

    /**
     * Constructor.
     */
    public KeyDoor() {
        this.isPassable = false;
        this.colour = "default";

        try {
            artAsset = new Image(new FileInputStream(filePath + colour + extension));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the colour of the door.
     *
     * @return String of the doors colour.
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the colour of a door.
     *
     * @param colour colour to be set.
     */
    public void setColour(String colour) {
        this.colour = colour;
        updateArtAsset();
    }

    /**
     * Updates the art asset to reflect the colour of the door if it is changed via setColour().
     */
    private void updateArtAsset() {
        try {
            artAsset = new Image(new FileInputStream(filePath + colour + extension));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the position of a door.
     *
     * @param position position ot be set.
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Opens the door if player has the correct key.
     */
    @Override
    public boolean open() {
        Item toBeRemoved = null;
        for (Item item : Map.getInstance().getPlayer().getInventory()) {
            if (item.getClass() == Key.class) {
                Key key = (Key) item;
                if (key.getColour().equals(this.colour)) {
                    toBeRemoved = item;
                    isPassable = true;
                }
            }
        }
        Map.getInstance().getPlayer().getInventory().remove(toBeRemoved);
        return isPassable;
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
     * Returns the passability of the cell.
     *
     * @return True if the Object can be walked through, else False.
     */
    @Override
    public boolean isPassable() {
        return isPassable;
    }

    /**
     * Returns the symbol depicting this cell.
     *
     * @return Symbol representation.
     */
    @Override
    public String getSymbol() {
        return symbol;
    }

    /**
     * Converts an instance of this class to a String.
     *
     * @return String instance.
     */
    @Override
    public String toString() {
        return "KDoor " + (int) position.getY() + " " + position.getX() + " " + colour;
    }
}
