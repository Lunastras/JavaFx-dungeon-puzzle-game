import javafx.scene.image.Image;

/**
 * This Class represents a generic Cell in a board style game.
 *
 * @author Harry Cassell
 * @author Dimitris Savva
 */
public abstract class Cell {
    protected String filePath;
    protected char symbol;
    protected boolean isPassable;

    /**
     * Gets the art asset of the Cell.
     *
     * @return Cells art asset as a JavFX Image.
     */
    public abstract Image getImage();

    /**
     * Checks whether the Cell can be walked over.
     *
     * @return True if can be walked over, else False.
     */
    public abstract boolean isPassable();

    /**
     * returns the symbol representation of the Cell.
     *
     * @return the symbol associated with the Cell.
     */
    public abstract String getSymbol();
}
