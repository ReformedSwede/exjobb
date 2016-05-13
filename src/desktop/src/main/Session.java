package main;

import java.io.Serializable;

/**
 * Represents a session for practising foreign words
 */
public class Session implements Serializable {
    private String nativeCode;
    private String foreignCode;
    private String title;

    /**
     *
     * @param n lowercase three letter code
     * @param f losercase three letter code
     * @param name
     */
    public Session(String n, String f, String name){
        nativeCode = n;
        foreignCode = f;
        title = name;
    }

    public String getNativeCode() {
        return nativeCode;
    }
    public String getForeignCode() {
        return foreignCode;
    }
    public String getTitle() {
        return title;
    }
}
