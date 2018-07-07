package de.unisaarland.sopra.utility;

/**
 * Created on 21.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class RegistrationAbortedException extends Exception {
    public RegistrationAbortedException() {
    }

    public RegistrationAbortedException(String s) {
        super(s);
    }

    public RegistrationAbortedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
