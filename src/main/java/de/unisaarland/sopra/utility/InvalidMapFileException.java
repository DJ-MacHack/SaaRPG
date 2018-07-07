package de.unisaarland.sopra.utility;

/**
 * Created by Team14 on 9/13/16.
 */
public class InvalidMapFileException extends Exception {

    public InvalidMapFileException() {
    }

    public InvalidMapFileException(String s) {
        super(s);
    }

    public InvalidMapFileException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
