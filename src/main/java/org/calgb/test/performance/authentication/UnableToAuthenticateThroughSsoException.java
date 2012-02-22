package org.calgb.test.performance.authentication;

public class UnableToAuthenticateThroughSsoException extends Exception {

    public UnableToAuthenticateThroughSsoException()
        {
            super();
        }

    public UnableToAuthenticateThroughSsoException(String message, Throwable cause)
        {
            super(message, cause);
        }

    public UnableToAuthenticateThroughSsoException(String message)
        {
            super(message);
        }

    public UnableToAuthenticateThroughSsoException(Throwable cause)
        {
            super(cause);
        }

}
