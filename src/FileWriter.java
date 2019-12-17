import java.io.BufferedWriter;
import java.io.IOException;

/**
 * This Class models a File Writer, with this class you can write the contents of files.
 *
 * @author Harry Cassell
 */
public class FileWriter extends FileManager {
    /**
     * Writes data to a profile.
     *
     * @param profile profile to write data from.
     */
    public static void writeProfile(Profile profile) {
        String path = PROFILE_FILE_PATH + profile.getName() + FILE_EXTENSION;
        String data = profile.toString();
        writeData(path, data);
    }

    /**
     * Writes a save state for a profile.
     *
     * @param profileName profile to be written for.
     * @param mapData     save state data to be written.
     */
    public static void writeSaveState(String profileName, String mapData) {
        String path = SAVE_FILE_PATH + profileName + SAVE_STATE + FILE_EXTENSION;
        writeData(path, mapData);
    }

    /**
     * Writes a player score to a score file.
     *
     * @param scoreFileName name of the score file.
     * @param score         score to be written.
     */

    public static void writeScore(String scoreFileName, String score) {
        String path = SCORE_FILE_PATH + scoreFileName + FILE_EXTENSION;

        try {
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(path, true));
            writer.write(score);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the parsed in data to the file at the parsed in file path.
     *
     * @param path file to be written to.
     * @param data data to be written.
     */
    private static void writeData(String path, String data) {
        if (path == null || data == null) {
            return;
        }

        try {
            java.io.FileWriter writer = new java.io.FileWriter(path);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
