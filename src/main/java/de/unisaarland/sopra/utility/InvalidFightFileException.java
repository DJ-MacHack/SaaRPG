package de.unisaarland.sopra.utility;

/**
 * Created by Team14 on 9/13/16.
 */
public class InvalidFightFileException extends Exception {

    public InvalidFightFileException() {
    }

    public InvalidFightFileException(String s) {
        super(s);
    }

    public InvalidFightFileException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
