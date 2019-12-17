import javafx.scene.image.Image;

/**
 * This class handles the sprite animation of the player.
 * <p>
 * Code originally from https://github.com/foreignguymike/legacyYTtutorials/tree/master/dragontale ,
 * but modified for the purposes of CS-230 A2.
 *
 * @author ForeignGuyMike
 * @author Radu Bucurescu
 */
public class SpriteAnimation {
    private Image[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    /**
     * Initialise the animator for the player's sprite.
     */
    public SpriteAnimation() {
        playedOnce = false;
    }

    /**
     * Set the frames of the current animation.
     *
     * @param frames The sprites.
     */
    public void setFrames(Image[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }

    /**
     * Set the delay between frames.
     *
     * @param delay The time in milliseconds.
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
     * Set the current frame of the animation.
     *
     * @param i The index of the frame.
     */
    public void setFrame(int i) {
        currentFrame = i;
    }

    /**
     * Update the animation.
     */
    public void update() {
        if (delay == -1) {
            return;
        }

        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if (currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    /**
     * Get the index of the current frame of the animation.
     *
     * @return The index.
     */
    public int getFrame() {
        return currentFrame;
    }

    /**
     * Get the current frame in the animation.
     *
     * @return The frame.
     */
    public Image getImage() {
        return frames[currentFrame];
    }

    /**
     * Checks if the animation has played at least once.
     *
     * @return playedOnce.
     */
    public boolean hasPlayedOnce() {
        return playedOnce;
    }
}
