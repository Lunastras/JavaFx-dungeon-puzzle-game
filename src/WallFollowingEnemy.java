import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Class used for basic wall following walking enemy.
 *
 * @author Radu Bucurescu
 * @author Harry Cassell
 * @author Peter Hawkins
 */
public class WallFollowingEnemy extends Enemy {
    private String direction; //Direction enemy faces e.g "U" = Up
    private Point2D position; //Position of enemy.
    private Cell[][] currentMapState = Map.getInstance().getCellArray(); // MapState
    private String filePath = "assets/images/WallFollowingEnemy.png";
    private static Image artAsset;
    private boolean hasToRedirect = false; //if the enemy has to change his direction.
    private boolean hasRedirected = true;
    private int numberOfChecks; //stores the number of checks we perform on his diagonal cells.


    /**
     * Construct a new WallFollowingEnemy().
     *
     * @param point2D   value to set a default point2D object.
     * @param direction starting direction of the enemy.
     */
    public WallFollowingEnemy(Point2D point2D, String direction) {
        this.position = point2D;
        this.direction = direction;
        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
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
        Point2D nextMove = null;
        numberOfChecks = 0;

        switch (direction) {
            case "U":
                if (currentMapState[currentY - 1][currentX].getClass() != Ground.class) {
                    hasToRedirect = true;
                    hasRedirected = false;
                    if (currentMapState[currentY][currentX - 1].getClass() == Ground.class) {
                        nextMove = new Point2D(-1, 0);
                    } else if (currentMapState[currentY][currentX + 1].getClass() == Ground.class) {
                        nextMove = new Point2D(1, 0);
                    } else {
                        return new Point2D(0, 1);
                    }
                }
                if (nextMove == null) {
                    nextMove = checkUpLeft(currentX, currentY);
                }
                break;
            case "D":
                if (currentMapState[currentY + 1][currentX].getClass() != Ground.class) {
                    hasToRedirect = true;
                    hasRedirected = false;
                    if (currentMapState[currentY][currentX + 1].getClass() == Ground.class) {
                        nextMove = new Point2D(+1, 0);
                    } else if (currentMapState[currentY][currentX - 1].getClass() == Ground.class) {
                        nextMove = new Point2D(-1, 0);
                    } else {
                        return new Point2D(0, -1);
                    }
                }
                if (nextMove == null) {
                    nextMove = checkDownRight(currentX, currentY);
                }
                break;
            case "R":
                if (currentMapState[currentY][currentX + 1].getClass() != Ground.class) {
                    hasToRedirect = true;
                    hasRedirected = false;
                    if (currentMapState[currentY + 1][currentX].getClass() == Ground.class) {
                        nextMove = new Point2D(0, 1);
                    } else if (currentMapState[currentY - 1][currentX].getClass() == Ground.class) {
                        nextMove = new Point2D(0, -1);
                    } else {
                        return new Point2D(-1, 0);
                    }
                }
                if (nextMove == null) {
                    nextMove = checkUpRight(currentX, currentY);
                }
                break;
            case "L":
                if (currentMapState[currentY][currentX - 1].getClass() != Ground.class) {
                    hasToRedirect = true;
                    hasRedirected = false;
                    if (currentMapState[currentY - 1][currentX].getClass() == Ground.class) {
                        nextMove = new Point2D(0, -1);
                    } else if (currentMapState[currentY + 1][currentX].getClass() == Ground.class) {
                        nextMove = new Point2D(0, 1);
                    } else {
                        return new Point2D(1, 0);
                    }
                }
                if (nextMove == null) {
                    nextMove = checkDownLeft(currentX, currentY);
                }
                break;
        }
        if (nextMove != null) {
            return nextMove;
        } else {
            switch (direction) {
                case "U":
                    return new Point2D(0, -1);
                case "D":
                    return new Point2D(0, 1);
                case "R":
                    return new Point2D(1, 0);
                case "L":
                    return new Point2D(-1, 0);
            }
            return new Point2D(0, 0);
        }
    }

    /**
     * Checks the upper left corner to consider its next position.
     *
     * @param currentX enemies current x coordinate.
     * @param currentY enemies current y coordinate.
     * @return The direction of the movement if it's executable, null otherwise.
     */
    private Point2D checkUpLeft(int currentX, int currentY) {
        if (currentMapState[currentY - 1][currentX - 1].getClass() != Ground.class) {
            if (direction.equals("R")) {
                if (currentMapState[currentY - 1][currentX].getClass() == Ground.class) {
                    return new Point2D(0, -1);
                } else if (currentMapState[currentY][currentX - 1].getClass() == Ground.class) {
                    return new Point2D(-1, 0);
                }
            } else if (currentMapState[currentY][currentX - 1].getClass() == Ground.class) {
                return new Point2D(-1, 0);
            } else if (currentMapState[currentY - 1][currentX].getClass() == Ground.class) {
                return new Point2D(0, -1);
            }
        }
        numberOfChecks++;
        if (numberOfChecks == 2 && !hasToRedirect) {
            hasToRedirect = true;
            hasRedirected = false;
            return null;
        }
        if ("R".equals(direction)) {
            return null;
        }
        return checkUpRight(currentX, currentY);
    }

    /**
     * Checks the lower left corner to consider its next position.
     *
     * @param currentX The enemie's current x coordinate.
     * @param currentY The enemie's current y coordinate.
     * @return The direction of the movement if it's executable, null otherwise.
     */
    private Point2D checkDownLeft(int currentX, int currentY) {
        if (currentMapState[currentY + 1][currentX - 1].getClass() != Ground.class) {
            if (direction.equals("R")) {
                if (currentMapState[currentY + 1][currentX].getClass() == Ground.class) {
                    return new Point2D(0, 1);
                } else if (currentMapState[currentY][currentX - 1].getClass() == Ground.class) {
                    return new Point2D(-1, 0);
                }
            } else if (currentMapState[currentY][currentX - 1].getClass() == Ground.class) {
                return new Point2D(-1, 0);
            } else if (currentMapState[currentY + 1][currentX].getClass() == Ground.class) {
                return new Point2D(0, 1);
            }
        }
        numberOfChecks++;
        if (numberOfChecks == 2 && !hasToRedirect) {
            hasToRedirect = true;
            hasRedirected = false;
            return null;
        }
        if ("U".equals(direction)) {
            return null;
        }
        return checkUpLeft(currentX, currentY);
    }

    /**
     * Checks the upper right corner to consider its next position.
     *
     * @param currentX enemies current x coordinate.
     * @param currentY enemies current y coordinate.
     * @return The direction of the movement if it's executable, null otherwise.
     */
    private Point2D checkUpRight(int currentX, int currentY) {
        if (currentMapState[currentY - 1][currentX + 1].getClass() != Ground.class) {
            if (direction.equals("L")) {
                if (currentMapState[currentY - 1][currentX].getClass() == Ground.class) {
                    return new Point2D(0, -1);
                } else if (currentMapState[currentY][currentX + 1].getClass() == Ground.class) {
                    return new Point2D(1, 0);
                }
            } else if (currentMapState[currentY][currentX + 1].getClass() == Ground.class) {
                return new Point2D(1, 0);
            } else if (currentMapState[currentY - 1][currentX].getClass() == Ground.class) {
                return new Point2D(0, -1);
            }
        }
        numberOfChecks++;
        if (numberOfChecks == 2 && !hasToRedirect) {
            hasToRedirect = true;
            hasRedirected = false;
            return null;
        }
        if ("D".equals(direction)) {
            return null;
        }
        return checkDownRight(currentX, currentY);
    }

    /**
     * Checks the lower right corner to consider its next position.
     *
     * @param currentX The enemie's current x coordinate.
     * @param currentY The enemie's current y coordinate.
     * @return The direction of the movement if it's executable, null otherwise.
     */
    private Point2D checkDownRight(int currentX, int currentY) {
        if (currentMapState[currentY + 1][currentX + 1].getClass() != Ground.class) {
            if (direction.equals("L")) {
                if (currentMapState[currentY + 1][currentX].getClass() == Ground.class) {
                    return new Point2D(0, 1);
                } else if (currentMapState[currentY][currentX + 1].getClass() == Ground.class) {
                    return new Point2D(1, 0);
                }
            } else if (currentMapState[currentY][currentX + 1].getClass() == Ground.class) {
                return new Point2D(1, 0);
            } else if (currentMapState[currentY + 1][currentX].getClass() == Ground.class) {
                return new Point2D(0, 1);
            }
        }
        numberOfChecks++;
        if (numberOfChecks == 2 && !hasToRedirect) {
            hasToRedirect = true;
            hasRedirected = false;
            return null;
        }
        if ("L".equals(direction)) {
            return null;
        }
        return checkDownLeft(currentX, currentY);
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
     * Method to retrieve point2D object.
     *
     * @return the position of enemy.
     */
    @Override
    public Point2D getPosition() {
        return position;
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
     * Updates the enemies position and direction.
     *
     * @param pos The direction to which the enemy is moving.
     */
    public void updatePosition(Point2D pos) {
        String newDirection;
        if (pos.getX() > 0) {
            setDirection("R");
        } else if (pos.getX() < 0) {
            setDirection("L");
        } else if (pos.getY() > 0) {
            setDirection("D");
        } else if (pos.getY() < 0) {
            setDirection("U");
        }
        if (!hasRedirected) {
            hasRedirected = true;
        } else {
            hasRedirected = false;
            hasToRedirect = false;
        }
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
        return "Enemy " + (int) position.getY() + " " + (int) position.getX() + " WF " + direction;
    }
}
