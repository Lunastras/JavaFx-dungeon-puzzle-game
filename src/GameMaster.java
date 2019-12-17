import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * The class is the main controller of the game.
 *
 * @author Radu Bucurescu
 */
public class GameMaster {
    private MapRenderer render;
    private Thread mainThread;
    private Scene scene;
    private Map map;
    //test values for the player animation
    private SpriteAnimation player;
    private final float ANIMATION_TIME = 0.3f;
    private TileMap tileset;
    private int playerOrientation = 0;
    //Variables used for the teleportation of the player.
    private boolean isTeleporting;
    private Point2D teleportPoint; //The teleporter to which the player is headed.
    private Point2D teleportDirection; //Used to determine where the player needs to head for the teleportation.
    private Point2D finalTeleportDirection; //Keeps the direction where the player needs to go after being teleported.

    /**
     * Initialise the sprite animation for the player.
     */
    private void createSpriteAnimation() {

        player = new SpriteAnimation();
        player.setDelay(render.getAnimationTime() / 3);
        player.setFrames(tileset.getSplices(0));
        player.setFrame(2);
        render.setPlayerSprite(player.getImage());
    }

    /**
     * Initialise the game.
     *
     * @param WINDOW_WIDTH  The width of the window.
     * @param WINDOW_HEIGHT The height of the window.
     * @param map           The map of the game.
     */
    public GameMaster(final int WINDOW_WIDTH, final int WINDOW_HEIGHT, Map map) {
        // Build the GUI
        // The root for the scene.
        Pane root = new StackPane();
        render = new MapRenderer(root, WINDOW_WIDTH, WINDOW_HEIGHT, map, this);
        this.map = map;
        render.setAnimationTime(ANIMATION_TIME);

        // Create a scene from the GUI
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Register an event handler for key presses
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::processKeyEvent);

        tileset = new TileMap(MapRenderer.GRID_CELL_SIDE);
        mainThread = Thread.currentThread();

        createSpriteAnimation();
        new Thread(animatePlayerSprite).start();
    }

    /**
     * Process a key event due to a key being pressed, e.g., to the player.
     *
     * @param event The key event that was pressed.
     */
    public void processKeyEvent(KeyEvent event) {
        if (render.canMove() && !isTeleporting) {
            int x = 0;
            int y = 0;
            switch (event.getCode()) {
                case RIGHT:
                    x = 1;
                    playerOrientation = 1;
                    break;
                case LEFT:
                    x = -1;
                    playerOrientation = 1;
                    break;
                case UP:
                    y = -1;
                    playerOrientation = 2;
                    break;
                case DOWN:
                    y = 1;
                    playerOrientation = 0;
                    break;
                default:
                    break;
            }
            if (x != 0 || y != 0) {
                processKeyPress(x, y);
            }
        }

        if (event.getCode() == KeyCode.ESCAPE) {
            if (render.isPaused()) {
                render.unPause();
            } else {
                render.pause();
            }
        }
        event.consume();
    }

    /**
     * If the input is valid, this method will be called to perform the action on the player.
     *
     * @param x The x coordinate on which the player moves.
     * @param y The y coordinate on which the player moves.
     */
    private void processKeyPress(int x, int y) {
        Point2D player = map.getPlayer().getPosition();
        Cell candidateCell = map.getCellArray()[(int) player.getY() + y][(int) player.getX() + x];
        boolean canExecuteMove = false;

        if (candidateCell.isPassable()) {
            if (candidateCell.getClass() == Ground.class) {
                canExecuteMove = true;
            } else if (candidateCell.getClass() == Teleporter.class) {
                isTeleporting = true;
                //Initialise the values for the teleporter.
                teleportCellLogic(x, y, (Teleporter) candidateCell);
                canExecuteMove = true;
            } else {
                canExecuteMove = true;
            }
        } else if (candidateCell.getClass() == TokenDoor.class) {
            canExecuteMove = tokenDoorAction(x, y, player, candidateCell);
        } else if (candidateCell.getClass() == KeyDoor.class) {
            canExecuteMove = keyDoorAction(x, y, player, candidateCell);
        }
        if (canExecuteMove) {
            boolean willDie = checkCollisionWithEnemies(x, y);
            executeAnimation();
            render.animatePlayerMove(x, y, willDie);
        }
    }

    /**
     * Check if the player will encounter an enemy on his path.
     *
     * @param moveX The x coordinate on which the player moves.
     * @param moveY The y coordinate on which the player moves.
     * @return whether a player has collided into an enemy or not.
     */
    public boolean checkCollisionWithEnemies(int moveX, int moveY) {
        if (isTeleporting) {
            return false;
        }
        Point2D player = Map.getInstance().getPlayer().getPosition();
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();
        ArrayList<Enemy> enemies = map.getEnemyArray();

        for (Enemy enemy : enemies) {
            Point2D pos = enemy.getPosition();
            if ((moveX + playerX) == (int) pos.getX() && (moveY + playerY) == (int) pos.getY()) {
                Point2D enemyMove = enemy.getNextMove();
                if ((playerX) == (int) (pos.getX() + enemyMove.getX()) && (playerY) == (int) (pos.getY() + enemyMove.getY())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Execute the animation for the player's sprite.
     */
    private void executeAnimation() {
        player.setFrames(tileset.getSplices(playerOrientation));
        player.setFrame(0);
    }

    /**
     * Check the current tile the player stepped on.
     */
    public void checkGround() {
        Point2D pos = map.getPlayer().getPosition();

        checkForItems(pos);
        checkForEnemies(pos);

        Cell currentCell = map.getCellArray()[(int) pos.getY()][(int) pos.getX()];

        if (currentCell.getClass() == Fire.class) {
            if (!((Fire) currentCell).playerHasItem()) {
                death();
            }
        } else if (currentCell.getClass() == Water.class) {
            if (!((Water) currentCell).playerHasItem()) {
                death();
            }
        } else if (currentCell.getClass() == Goal.class) {
            render.renderGoal();
            Map.getInstance().updateProfile();
            Map.getInstance().saveScore();
        }
    }

    /**
     * Check if there are enemies on the current tile.
     *
     * @param player The coordinates on which we are checking.
     */
    public void checkForEnemies(Point2D player) {
        if (isTeleporting) {
            return;
        }

        int x = (int) player.getX();
        int y = (int) player.getY();
        ArrayList<Enemy> enemies = map.getEnemyArray();

        for (Enemy enemy : enemies) {
            Point2D pos = enemy.getPosition();
            if (x == pos.getX() && y == pos.getY()) {
                death();
                break;
            }
        }
    }

    /**
     * Check the ground for items that can be picked up.
     *
     * @param player The player's coordinates.
     */
    public void checkForItems(Point2D player) {
        if (isTeleporting) {
            return;
        }

        int x = (int) player.getX();
        int y = (int) player.getY();
        ArrayList<Item> items = map.getItemArray();

        for (Item item : items) {
            Point2D pos = item.getPosition();
            if (x == pos.getX() && y == pos.getY()) {
                map.getPlayer().getInventory().add(item);
                items.remove(item);
                break;
            }
        }
    }

    /**
     * The action for the colored key door.
     *
     * @param x             coordinate on which the player moves.
     * @param y             coordinate on which the player moves.
     * @param player        coordinates of the player.
     * @param candidateCell cell we are checking.
     * @return true if the move is executable.
     */
    private boolean keyDoorAction(int x, int y, Point2D player, Cell candidateCell) {
        if (((KeyDoor) candidateCell).open()) {
            //Change the door into a Ground tile.
            map.getCellArray()[(int) player.getY() + y][(int) player.getX() + x] = new Ground();
            render.drawGame(0, 0);
            return true;
        }
        return false;
    }


    /**
     * The action for the token door.
     *
     * @param x             coordinate on which the player moves.
     * @param y             coordinate on which the player moves.
     * @param player        coordinates of the player.
     * @param candidateCell cell we are checking.
     * @return true if the move is executable.
     */
    private boolean tokenDoorAction(int x, int y, Point2D player, Cell candidateCell) {
        int requiredTokens = ((TokenDoor) candidateCell).getNumberOfTokens();
        int numOfTokens = 0;
        ArrayList<Item> inventory = map.getPlayer().getInventory();

        for (Item item : inventory) {
            if (item.getClass() == Token.class) {
                numOfTokens++;
                //If it found all the tokens, stop the search.
                if (numOfTokens == requiredTokens) {
                    //Change the door into a Ground tile.
                    map.getCellArray()[(int) player.getY() + y][(int) player.getX() + x] = new Ground();
                    render.drawGame(0, 0);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The setup for the teleportation of the player.
     *
     * @param x          coordinate on which the player moves.
     * @param y          coordinate on which the player moves.
     * @param teleporter teleporter the player stepped on.
     */
    private void teleportCellLogic(int x, int y, Teleporter teleporter) {
        teleportPoint = teleporter.getLink().getPosition();
        int xDir = (int) (teleporter.getPosition().getX() - teleportPoint.getX());
        int yDir = (int) (teleporter.getPosition().getY() - teleportPoint.getY());
        teleportDirection = new Point2D(0, 0);

        if (xDir > 0) {
            teleportDirection = new Point2D(-1, 0);
        } else if (xDir < 0) {
            teleportDirection = new Point2D(1, 0);
        }

        if (yDir > 0) {
            teleportDirection = new Point2D(teleportDirection.getX(), -1);
        } else if (yDir < 0) {
            teleportDirection = new Point2D(teleportDirection.getX(), 1);
        }

        Point2D finalDest = teleporter.getTeleportDestination(x, y);

        if (finalDest == null) {
            isTeleporting = false;
        } else {
            finalTeleportDirection = new Point2D(finalDest.getX(), finalDest.getY());
        }
    }

    /**
     * Please don't call this method without a safe environment set up.
     * Moves the player towards the teleport goal.
     */
    public void teleportPlayer() {
        Point2D pos = map.getPlayer().getPosition();
        render.makePlayerInvisible();
        float TELEPORT_TIME = 0.1f;
        render.setAnimationTime(TELEPORT_TIME);

        if (Math.abs(pos.getX()) != Math.abs(teleportPoint.getX())) {
            render.animatePlayerMove((int) teleportDirection.getX(), 0, false);
        } else if (Math.abs(pos.getY()) != Math.abs(teleportPoint.getY())) {
            render.animatePlayerMove(0, (int) teleportDirection.getY(), false);
        } else {
            render.makePlayerVisible();
            isTeleporting = false;
            render.setAnimationTime(ANIMATION_TIME);
            executeAnimation();
            render.animatePlayerMove((int) finalTeleportDirection.getX(), (int) finalTeleportDirection.getY(), false);
        }
    }

    /**
     * Kill the player.
     */
    private void death() {
        render.playerDeath();
    }

    /**
     * The animator for the player's sprites.
     */
    Task<Void> animatePlayerSprite = new Task<Void>() {
        @Override
        public Void call() {
            while (mainThread.isAlive()) {
                try {
                    if (!player.hasPlayedOnce()) {
                        player.update();
                        render.setPlayerSprite(player.getImage());
                    }
                    long checkSpriteInterval = 100;
                    Thread.sleep(checkSpriteInterval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    };

    /**
     * Get the scene of the game.
     *
     * @return The scene.
     */
    public Scene getScene() {
        return this.scene;
    }


    /**
     * Checks if the player is currently being teleported.
     *
     * @return isTeleporting.
     */
    public boolean isTeleporting() {
        return isTeleporting;
    }
}
