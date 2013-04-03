package org.calgb.test.performance;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpRequestBuilder<T extends HttpEntityEnclosingRequestBase> {

    private T request;

    public HttpRequestBuilder(T request)
        {
            this.request = request;
        }
    
    
    
   
}
