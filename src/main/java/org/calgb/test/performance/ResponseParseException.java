package org.calgb.test.performance;

public class ResponseParseException extends Exception{

    public ResponseParseException(String message, Throwable cause)
        {
            super(message, cause);
        }
    public ResponseParseException( Throwable cause)
        {
            super("failed to parse response", cause);
        }

}
