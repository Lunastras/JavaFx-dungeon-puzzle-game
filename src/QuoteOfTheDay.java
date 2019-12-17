import java.io.*;
import java.net.*;

/**
 * A class to retrieve quote of the day from 2 different web pages.
 *
 * @author Peter Hawkins
 */
public class QuoteOfTheDay {
    private static QuoteOfTheDay instance;
    private static final String KEY_URL = "http://cswebcat.swan.ac.uk/puzzle"; //Url to retrieve puzzle string
    private static final String MESSAGE_URL = "http://cswebcat.swan.ac.uk/message?solution="; //Url to send decrypted puzzle

    /**
     * Method to return instance of QuoteOfTheDay object.
     *
     * @return Quote of the day instance object.
     */
    public static QuoteOfTheDay getInstance() {
        if (instance == null) {
            instance = new QuoteOfTheDay();
        }
        return instance;
    }

    /**
     * Method to delete the quote of the day object.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Method to get the puzzle string from the website.
     *
     * @return returns the puzzle string from the website.
     * @throws Exception any errors return null.
     */
    private String getKeyUrl() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(KEY_URL);
        return getString(result, url);
    }

    /**
     * Method to get the quote of the day from the website.
     *
     * @param decrypt passes the decrypted puzzle string for the solution to be sent to quote website.
     * @return returns the quote of the day string from the website.
     * @throws Exception any errors return null.
     */
    private String getMessageUrl(String decrypt) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(MESSAGE_URL + decrypt);
        return getString(result, url);
    }

    /**
     * Method that handles all the html web page GET requests and the contents of those pages.
     *
     * @param result used to collect the puzzle from the first web page.
     * @param url    passes the desired url whether its for the puzzle/the quote of the day.
     * @return returns the string from the websites and adds them to a string for use.
     * @throws IOException error with input or output.
     */
    private String getString(StringBuilder result, URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    /**
     * Method to decrypt the puzzle string.
     *
     * @param puzzle pass in the string from the getKeyUrl() function.
     * @return returns the decrypted puzzle string.
     */
    private String decryptPuzzle(String puzzle) {
        final int SHIFT = 1;
        StringBuilder decrypt = new StringBuilder();
        for (int i = 0; i < puzzle.length(); i++) {
            char charAt = (puzzle.charAt(i));
            if (i % 2 == 0) {
                if (charAt == 'Z') {
                    decrypt.append('A');
                } else {
                    decrypt.append((char) (puzzle.charAt(i) + SHIFT));
                }
            } else {
                if (charAt == 'A') {
                    decrypt.append('Z');
                } else {
                    decrypt.append((char) (puzzle.charAt(i) - SHIFT));
                }
            }
        }
        return decrypt.toString();
    }

    /**
     * Method that's public to retrieve the message of the day.
     *
     * @return returns the quote of the day from the web address.
     * @throws Exception any errors return null.
     */
    public String sendMOTD() throws Exception {
        return getMessageUrl(decryptPuzzle(getKeyUrl()));
    }
}
