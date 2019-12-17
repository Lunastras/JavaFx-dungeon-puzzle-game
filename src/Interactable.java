/**
 * This Class models a Cell that can be interacted with by the player.
 *
 * @author Dimitris Savva
 */
public abstract class Interactable extends Cell {
    /**
     * Checks to see if the player has the required item to change the state of the cell.
     *
     * @return true/false if player has the item.
     */
    public abstract boolean playerHasItem();
}
