package main;

import java.util.List;

/**
 * Interface containing a callback method. Used to enable the "inflection dialog" to call back to the main window.
 */
public interface InflectionCallback {
    /**
     * Forwards information about a Word that has been updated
     * @param category The category of the word
     * @param originalWord The previously default form of the word
     * @param nativeList The updated list of all native inflections
     * @param foreignList The updated list of all foreign inflections
     */
    void call(String category, String originalWord, List<String> nativeList, List<String> foreignList);

    //TODO Check if 'originalWord' above should be function? What if the original word differs from the fun name???
}
