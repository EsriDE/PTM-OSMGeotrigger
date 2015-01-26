package de.esri.geotrigger.core;

public class TokenException extends Exception {
    public TokenException () {

    }

    public TokenException (String message) {
        super (message);
    }

    public TokenException (Throwable cause) {
        super (cause);
    }

    public TokenException (String message, Throwable cause) {
        super (message, cause);
    }
}