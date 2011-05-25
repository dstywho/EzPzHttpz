package org.calgb.test.performance;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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

    public void setResponse(org.apache.http.HttpResponse response)
        {
            this.response = response;
        }

    public SimplifiedResponse(HttpResponse response) throws ProcessResponseBodyException
        {
            StatusLine statusLine = response.getStatusLine();
            code = statusLine.getStatusCode();
            message = statusLine.getReasonPhrase();
            try
                {
                    body = EntityUtils.toString(response.getEntity());
                }
            catch (Exception e)
                {
                    throw new ProcessResponseBodyException(e);
                }

        }

    public SimplifiedResponse(int code, String message, String body, org.apache.http.HttpResponse response)
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

    public void setMessage(String message)
        {
            this.message = message;
        }

    public String getBody()
        {
            return body;
        }

    public void setBody(String body)
        {
            this.body = body;
        }

    public int getCode()
        {
            return code;
        }

    public void setCode(int code)
        {
            this.code = code;
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
