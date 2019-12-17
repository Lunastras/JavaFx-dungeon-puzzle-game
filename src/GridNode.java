/**
 * Class represents a node in A* algorithm pathfinder.
 */
public class GridNode {
    private int heuristicCost = 0;
    private int finalCost = 0; //G+H.
    private int gridNodeX;
    private int gridNodeY;
    private GridNode parent;

    /**
     * Constructs a GridNode.
     *
     * @param x value of the gridNode.
     * @param y value of the gridNode.
     */
    GridNode(int x, int y) {
        this.gridNodeX = x;
        this.gridNodeY = y;
    }

    /**
     * Prints out the location of the gridNode.
     *
     * @return x and y values of gridNode.
     */
    @Override
    public String toString() {
        return "[" + this.gridNodeX + ", " + this.gridNodeY + "]";
    }

    /**
     * Gets the Heuristic cost of the node.
     *
     * @return heuristicCost value up to this node.
     */
    public int getHeuristicCost() {
        return heuristicCost;
    }

    /**
     * Sets the Heuristic cost for this node.
     *
     * @param heuristicCost heuristic value cost to set for the node.
     */
    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    /**
     * Gets the final cost of movement + direction (G + H).
     *
     * @return the final cost in gridNode.
     */
    public int getFinalCost() {
        return finalCost;
    }

    /**
     * Sets the final cost for the gridNode.
     *
     * @param finalCost value to set for the node.
     */
    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }

    /**
     * Get GridNodes x position.
     *
     * @return gridNode x value.
     */
    public int getGridNodeX() {
        return gridNodeX;
    }

    /**
     * Sets GridNode X position.
     *
     * @param GridNodeX x value to set.
     */
    public void setGridNodeX(int GridNodeX) {
        this.gridNodeX = GridNodeX;
    }

    /**
     * Get GridNodes y position.
     *
     * @return gridNode y value.
     */
    public int getGridNodeY() {
        return gridNodeY;
    }

    /**
     * Sets GridNode y position.
     *
     * @param gridNodeY y value to set.
     */
    public void setGridNodeY(int gridNodeY) {
        this.gridNodeY = gridNodeY;
    }

    /**
     * Gets the previous GridNode to this GridNode object.
     *
     * @return THe parent/previous GridNode of current.
     */
    public GridNode getParent() {
        return parent;
    }

    /**
     * Sets the previous GridNode to this GridNode object.
     *
     * @param parent set link between this node and the next.
     */
    public void setParent(GridNode parent) {
        this.parent = parent;
    }
}
