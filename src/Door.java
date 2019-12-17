/**
 * This Class models generic door.
 *
 * @author Harry Cassell
 * @author Dimitris Savva
 */
public abstract class Door extends Cell {
    /**
     * Opens the door.
     *
     * @return true/false dependent on if the door is open.
     */
    public abstract boolean open();
}
