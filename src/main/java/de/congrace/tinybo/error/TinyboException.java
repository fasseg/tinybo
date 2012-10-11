package de.congrace.tinybo.error;

public class TinyboException extends Exception {
    private static final long serialVersionUID = 1L;

    public TinyboException(String msg) {
        super(msg);
    }

    public TinyboException(String msg, Throwable t) {
        super(msg, t);
    }
}
