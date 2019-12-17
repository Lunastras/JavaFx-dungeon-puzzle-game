import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;

/**
 * Class used for basic straight line walking enemy.
 *
 * @author Peter Hawkins
 * @author Harry Cassell
 */
public class StraightLineEnemy extends Enemy {
    private String direction; // Direction enemy faces e.g "U" = Up
    private Point2D position; // Position of enemy.
    private Cell[][] currentMapState = Map.getInstance().getCellArray();
    private String filePath = "assets/images/StraightLineEnemy.png";
    private Image artAsset;

    /**
     * Construct a new StraightLineEnemy().
     *
     * @param point2D   value to set a default point2D object.
     * @param direction initial direction enemy will travel.
     */
    public StraightLineEnemy(Point2D point2D, String direction) {
        this.position = point2D;
        this.direction = direction;
        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to calculate the enemies next move relative to the board state.
     *
     * @return enemies new position on the board.
     */
    @Override
    public Point2D getNextMove() {
        int currentX = (int) position.getX();
        int currentY = (int) position.getY();

        switch (direction) {
            case "U":
                if (!currentMapState[currentY - 1][currentX].isPassable()) {
                    this.setDirection("D");
                    return (new Point2D(0.0, 1.0));
                } else {
                    return (new Point2D(0.0, -1.0));
                }
            case "D":
                if (!currentMapState[currentY + 1][currentX].isPassable()) {
                    this.setDirection("U");
                    return (new Point2D(0.0, -1.0));
                } else {
                    return (new Point2D(0.0, 1.0));
                }
            case "L":
                if (!currentMapState[currentY][currentX - 1].isPassable()) {
                    this.setDirection("R");
                    return (new Point2D(1.0, 0.0));
                } else {
                    return (new Point2D(-1.0, 0.0));
                }
            case "R":
                if (!currentMapState[currentY][currentX + 1].isPassable()) {
                    this.setDirection("L");
                    return (new Point2D(-1.0, 0.0));
                } else {
                    return (new Point2D(1.0, 0.0));
                }
            default:
                return (new Point2D(0.0, 0.0));
        }
    }

    /**
     * Method to get direction enemy faces.
     *
     * @return the direction of enemy.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Change the direction of an enemy.
     *
     * @param direction current direction the enemy is travelling in.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Sets the position of the StraightLineEnemy.
     *
     * @param position new position of the enemy.
     */
    @Override
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Method to retrieve enemies position.
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
        return "Enemy " + (int) position.getY() + " " + (int) position.getX() + " SL " + direction;
    }
}
