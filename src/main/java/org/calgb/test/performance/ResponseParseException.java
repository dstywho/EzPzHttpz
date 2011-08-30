package org.calgb.test.performance;

public class ResponseParseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ResponseParseException(final String message, final Throwable cause)
        {
            super(message, cause);
        }

    public ResponseParseException(final Throwable cause)
        {
            super("failed to parse response", cause);
        }

}
