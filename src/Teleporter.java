import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class models a Teleporter Cell.
 *
 * @author Harry Cassell
 * @author Dimitris Savva
 * @author Radu Bucurescu
 */
public class Teleporter extends Cell {
    private String filePath = "assets/images/Teleporter.png";
    private Image artAsset;
    private final String symbol = "T";
    private boolean isPassable = true;
    private Point2D position;
    private Teleporter link;

    /**
     * Constructor.
     */
    public Teleporter() {
        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the position of the teleporter.
     *
     * @param position position ot be set.
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Returns the current position of the teleporter.
     *
     * @return teleporters position.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Sets the destination teleporter.
     *
     * @param link Teleporter to link with.
     */
    public void setLink(Teleporter link) {
        this.link = link;
    }

    /**
     * Returns the destination teleporter.
     *
     * @return Teleporter that is linked.
     */
    public Teleporter getLink() {
        return link;
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
     * Returns the direction in which the player teleports after being moved to the linked teleporter.
     *
     * @param x The x coordinate on which the player moves.
     * @param y The y coordinate on which the player moves.
     * @return The direction.
     */
    public Point2D getTeleportDestination(int x, int y) {
        int destinationX = (int) link.getPosition().getX();
        int destinationY = (int) link.getPosition().getY();
        Cell[][] currentMapState = Map.getInstance().getCellArray();

        //checks if the supposed teleport location is viable, if not, it will check in a clockwise direction.
        if (currentMapState[destinationY + y][destinationX + x].isPassable()) {
            return new Point2D(x, y);
        } else if (currentMapState[destinationY][destinationX + 1].isPassable()) {
            return new Point2D(1, 0);
        } else if (currentMapState[destinationY + 1][destinationX].isPassable()) {
            return new Point2D(0, 1);
        } else if (currentMapState[destinationY][destinationX - 1].isPassable()) {
            return new Point2D(-1, 0);
        } else if (currentMapState[destinationY - 1][destinationX].isPassable()) {
            return new Point2D(0, -1);
        }
        return null;
    }

    /**
     * Converts an instance of this class to a String.
     *
     * @return String instance.
     */
    @Override
    public String toString() {
        return "Tele " + (int) position.getY() + " " + (int) position.getX() + " " + (int) link.getPosition().getY()
                + " " + (int) link.getPosition().getX();
    }
}
