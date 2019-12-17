import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This Class models a ground cell.
 *
 * @author Harry Cassell.
 * @author Dimitris Savva.
 */
public class Ground extends Cell {
    private String filePath = "assets/images/Ground.png";
    private Image artAsset;
    private final String symbol = "_";
    private boolean isPassable = true;

    /**
     * Constructor.
     */
    public Ground() {
        try {
            artAsset = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
}
