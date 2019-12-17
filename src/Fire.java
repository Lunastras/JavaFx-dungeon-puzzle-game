import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This Class models a Fire Cell.
 *
 * @author Harry Cassell.
 * @author Dimitris Savva.
 */
public class Fire extends Interactable {
    private String filePath = "assets/images/Fire.png";
    private Image artAsset;
    private final String symbol = "F";
    private boolean isPassable;

    /**
     * Constructor.
     */
    public Fire() {
        this.isPassable = true;
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

    /**
     * Runs a check to see if the player has the required item to be able to pass through the cell.
     *
     * @return True if the player has the item, else False.
     */
    @Override
    public boolean playerHasItem() {
        for (Item item : Map.getInstance().getPlayer().getInventory()) {
            if (item.getClass() == FireBoots.class) {
                return true;
            }
        }
        return false;
    }
}
