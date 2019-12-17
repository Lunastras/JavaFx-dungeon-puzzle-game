import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Class used for enemy which targets the player in the best way possible.
 * <p>
 * Implementation of A* Algorithm for Shortest path.
 * Source: http://www.codebytes.in/2015/02/a-shortest-path-finding-algorithm.html.
 * Modified to suit our needs for smart Enemy AI.
 *
 * @author Peter Hawkins
 */
public class SmartTargetingEnemy extends Enemy {
    private Point2D position;
    private Cell[][] currentMapState = Map.getInstance().getCellArray();
    private String filePath = "assets/images/SmartTargetingEnemy.png";
    private Image artAsset;
    private final int DIAGONAL_COST = 14;
    private final int V_H_COST = 10;
    private GridNode[][] grid = new GridNode[0][0];
    private PriorityQueue<GridNode> open;
    private boolean[][] closed;
    private int startX;
    private int startY;
    private int goalX;
    private int goalY;

    /**
     * Construct a new SmartTargetingEnemy().
     *
     * @param position value to set a default point2D object.
     */
    public SmartTargetingEnemy(Point2D position) {
        this.position = position;
        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets next move for the Smart Enemy.
     *
     * @return either the next position based on A* Algorithm or Random valid position if no path.
     */
    @Override
    public Point2D getNextMove() {
        grid = new GridNode[Map.getInstance().getCellArray().length][Map.getInstance().getCellArray()[0].length];
        closed = new boolean[Map.getInstance().getCellArray().length][Map.getInstance().getCellArray()[0].length];

        open = new PriorityQueue<>((Object o1, Object o2) -> {
            GridNode gridNode1 = (GridNode) o1;
            GridNode gridNode2 = (GridNode) o2;
            return Integer.compare(gridNode1.getFinalCost(), gridNode2.getFinalCost());
        });

        setStartGridNode((int) position.getY(), (int) position.getX());
        setEndGridNode((int) Map.getInstance().getPlayer().getPosition().getY(), (int) Map.getInstance().getPlayer().getPosition().getX());

        for (int i = 0; i < Map.getInstance().getCellArray().length; ++i) {
            for (int j = 0; j < Map.getInstance().getCellArray()[0].length; ++j) {
                grid[i][j] = new GridNode(i, j);
                grid[i][j].setHeuristicCost(Math.abs(i - goalX) + Math.abs(j - goalY));
            }
        }

        grid[startX][startY].setFinalCost(0);

        for (int i = 0; i < Map.getInstance().getCellArray().length; i++) {
            for (int j = 0; j < Map.getInstance().getCellArray()[0].length; j++) {
                if (Map.getInstance().getCellArray()[i][j].getSymbol().equals("#")
                        || Map.getInstance().getCellArray()[i][j].getSymbol().equals("D")
                        || Map.getInstance().getCellArray()[i][j].getSymbol().equals("K")
                        || Map.getInstance().getCellArray()[i][j].getSymbol().equals("F")
                        || Map.getInstance().getCellArray()[i][j].getSymbol().equals("W")
                        || Map.getInstance().getCellArray()[i][j].getSymbol().equals("T")
                        || Map.getInstance().getCellArray()[i][j].getSymbol().equals("G")) {
                    grid[i][j] = null;
                }
            }
        }

        aStarPathfinder();

        if (closed[goalX][goalY]) {
            //Trace back the path
            ArrayList<Point2D> path = new ArrayList();
            GridNode current = grid[goalX][goalY];
            while (current.getParent() != null) {
                path.add(new Point2D(current.getGridNodeX(), current.getGridNodeY()));
                current = current.getParent();
            }
            double distX = path.get((path.size() - 1)).getX() - startX;
            double distY = path.get((path.size() - 1)).getY() - startY;
            return (new Point2D(distY, distX));
        } else {
            return alternateMove();
        }
    }

    /**
     * Implementation of A* path finding algorithm.
     */
    public void aStarPathfinder() {
        open.add(grid[startX][startY]);

        GridNode currentNode;

        while (true) {
            currentNode = open.poll();
            if (currentNode == null) {
                break;
            }
            closed[currentNode.getGridNodeX()][currentNode.getGridNodeY()] = true;
            if (currentNode.equals(grid[goalX][goalY])) {
                return;
            }

            GridNode gridNode;
            if (currentNode.getGridNodeX() - 1 >= 0) {
                gridNode = grid[currentNode.getGridNodeX() - 1][currentNode.getGridNodeY()];
                checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + V_H_COST);

                if (currentNode.getGridNodeY() - 1 >= 0) {
                    gridNode = grid[currentNode.getGridNodeX() - 1][currentNode.getGridNodeY() - 1];
                    checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + DIAGONAL_COST);
                }
                if (currentNode.getGridNodeY() + 1 < grid[0].length) {
                    gridNode = grid[currentNode.getGridNodeX() - 1][currentNode.getGridNodeY() + 1];
                    checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + DIAGONAL_COST);
                }
            }

            if (currentNode.getGridNodeY() - 1 >= 0) {
                gridNode = grid[currentNode.getGridNodeX()][currentNode.getGridNodeY() - 1];
                checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + V_H_COST);
            }
            if (currentNode.getGridNodeY() + 1 < grid[0].length) {
                gridNode = grid[currentNode.getGridNodeX()][currentNode.getGridNodeY() + 1];
                checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + V_H_COST);
            }
            if (currentNode.getGridNodeX() + 1 < grid.length) {
                gridNode = grid[currentNode.getGridNodeX() + 1][currentNode.getGridNodeY()];
                checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + V_H_COST);

                if (currentNode.getGridNodeY() - 1 >= 0) {
                    gridNode = grid[currentNode.getGridNodeX() + 1][currentNode.getGridNodeY() - 1];
                    checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + DIAGONAL_COST);
                }
                if (currentNode.getGridNodeY() + 1 < grid[0].length) {
                    gridNode = grid[currentNode.getGridNodeX() + 1][currentNode.getGridNodeY() + 1];
                    checkAndUpdateCost(currentNode, gridNode, currentNode.getFinalCost() + DIAGONAL_COST);
                }
            }
        }
    }

    /**
     * Gets the costs of current node and next node search then updates the cost values.
     *
     * @param current  node in the grid.
     * @param gridNode next gridNode in the grid checked.
     * @param cost     overall cost between these nodes.
     */
    private void checkAndUpdateCost(GridNode current, GridNode gridNode, int cost) {
        if (gridNode == null || closed[gridNode.getGridNodeX()][gridNode.getGridNodeY()]) {
            return;
        }
        int t_final_cost = gridNode.getHeuristicCost() + cost;

        boolean inOpen = open.contains(gridNode);
        if (!inOpen || t_final_cost < gridNode.getFinalCost()) {
            gridNode.setFinalCost(t_final_cost);
            gridNode.setParent(current);
            if (!inOpen) {
                open.add(gridNode);
            }
        }
    }

    /**
     * Alternate movement for when a path can't be found with the A* algorithm.
     *
     * @return alternate moves Point2d object, x and y coordinates.
     */
    public Point2D alternateMove() {
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
     * Sets the initial startNode/enemy position.
     *
     * @param x position of the enemy.
     * @param y position of the enemy.
     */
    public void setStartGridNode(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    /**
     * Sets the goalNode/playerPosition.
     *
     * @param x position of the player.
     * @param y position of the player.
     */
    public void setEndGridNode(int x, int y) {
        this.goalX = x;
        this.goalY = y;
    }

    /**
     * Sets Point2D position object for smart enemy.
     *
     * @param position of the smart enemy.
     */
    @Override
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Gets position of the smart enemy.
     *
     * @return Point2D position object.
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
    public String toString() {
        return "Enemy " + (int) position.getY() + " " + (int) position.getX() + " ST";
    }
}
