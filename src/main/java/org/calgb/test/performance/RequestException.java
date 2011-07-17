package org.calgb.test.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestException extends Exception {

    public static enum HTTP_METHODS {
        GET("GET"), POST("POST"), HEAD("HEAD"), DELETE("DELETE"), PUT("PUT"), TRACE("TRACE"), CONNECT("CONNECT");

        private HTTP_METHODS(String name)
            {
                this.name = name;
            }

        private final String name;

        public String toString()
            {
                return name;
            }
    }

    public static Logger LOG = LoggerFactory.getLogger(RequestException.class);

    public RequestException(String url, String params, HTTP_METHODS method, String message, Throwable cause)
        {
            LOG.error("Failed {} request to {}: ({}) {}", new Object[] { method.toString(), url, message, params });
        }

    public RequestException(String url, String params, HTTP_METHODS method, Throwable cause)
        {
            LOG.error("Failed {} request to {}: {}", new Object[] { method.toString(), url, params });
        }

    public RequestException(String url, HTTP_METHODS method, Throwable cause)
            LOG.error("Failed {} request to {}", new Object[] { method.toString(), url });
        }

}
