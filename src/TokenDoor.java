import javafx.scene.image.Image;
import javafx.geometry.Point2D;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This Class models a token door, a door that can be opened with tokens.
 *
 * @author Harry Cassell.
 * @author Dimitris Savva.
 */
public class TokenDoor extends Door {
    private String filePath = "assets/images/TokenDoor.png";
    private Image artAsset;
    private final String symbol = "D";
    private Point2D position;
    private boolean isPassable;
    private int numberOfTokens;

    /**
     * Constructor.
     */
    public TokenDoor() {
        isPassable = false;
        numberOfTokens = 0;

        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the required number of tokens for the door to be opened.
     *
     * @param numberOfTokens tokens required.
     */
    public void setNumberOfTokens(int numberOfTokens) {
        this.numberOfTokens = numberOfTokens;
    }

    /**
     * Retrieve the number of tokens that the door needs to be opened.
     *
     * @return number of tokens needed.
     */
    public int getNumberOfTokens() {
        return this.numberOfTokens;
    }

    /**
     * Set the position of the door.
     *
     * @param position position ot be set.
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Opens the door.
     */
    @Override
    public boolean open() {
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
        return "TDoor " + (int) position.getY() + " " + (int) position.getX() + " " + numberOfTokens;
    }
}
