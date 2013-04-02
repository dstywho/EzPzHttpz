package org.calgb.test.performance;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

public class SimplifiedResponse {

    private String                       message;
    private String                       body;
    private int                          code;
    private org.apache.http.HttpResponse response;

    public org.apache.http.HttpResponse getResponse()
        {
            return response;
        }

    public void setResponse(final org.apache.http.HttpResponse response)
        {
            this.response = response;
        }

    public SimplifiedResponse(final HttpResponse response) throws ProcessResponseBodyException
        {
            final StatusLine statusLine = response.getStatusLine();
            code = statusLine.getStatusCode();
            message = statusLine.getReasonPhrase();
            try
                {
                    body = EntityUtils.toString(response.getEntity());
                }
            catch (final Exception e)
                {
                    throw new ProcessResponseBodyException(e);
                }

        }

    public SimplifiedResponse(final int code, final String message, final String body, final org.apache.http.HttpResponse response)
        {
            super();
            this.message = message;
            this.body = body;
            this.code = code;
            this.response = response;
        }

    public String getMessage()
        {
            return message;
        }

    public void setMessage(final String message)
        {
            this.message = message;
        }

    public String getBody()
        {
            return body;
        }

    public void setBody(final String body)
        {
            this.body = body;
        }

    public int getCode()
        {
            return code;
        }

    public void setCode(final int code)
        {
            this.code = code;
        }

    public void closeResponse()
        {
            try
                {
                    this.response.getEntity().consumeContent();
                }
            catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
        }

    public void print()
        {
            System.out.println("----------START RESPOSNE----------");
            System.out.println("----------BODY----------");
            System.out.println(body);
            System.out.println("----------RESPONSE MESSAGE----------");
            System.out.println(code + ", " + message);
            System.out.println("----------END RESPONSE----------");
        }

}
