package org.calgb.test.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestException extends Exception {

    public static Logger LOG  = LoggerFactory.getLogger(RequestException.class);
    public RequestException(String params, String method, String message, Throwable cause)
        {
            super(message, cause);
            LOG.error("Failed {} request: {}", params);
        }
    public RequestException(String message, Throwable cause)
        {
            super(message, cause);
            // TODO Auto-generated constructor stub
        }

}
