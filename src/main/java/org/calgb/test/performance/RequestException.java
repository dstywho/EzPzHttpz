package org.calgb.test.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static enum HTTP_METHODS {
        GET("GET"), POST("POST"), HEAD("HEAD"), DELETE("DELETE"), PUT("PUT"), TRACE("TRACE"), CONNECT("CONNECT");

        private HTTP_METHODS(final String name)
            {
                this.name = name;
            }

        private final String name;

        @Override
        public String toString()
            {
                return name;
            }
    }

    public static Logger LOG = LoggerFactory.getLogger(RequestException.class);

    public RequestException(final String url, final String params, final HTTP_METHODS method, final String message, final Throwable cause)
        {
            LOG.error("Failed {} request to {}: ({}) {}", new Object[] { method.toString(), url, message, params });
        }

    public RequestException(final String url, final String params, final HTTP_METHODS method, final Throwable cause)
        {
            LOG.error("Failed {} request to {}: {}", new Object[] { method.toString(), url, params });
        }

    public RequestException(final String url, final HTTP_METHODS method, final Throwable cause)
        {
            LOG.error("Failed {} request to {}", new Object[] { method.toString(), url });
            LOG.error(cause.getMessage());
        }

}
