package org.calgb.test.performance;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

public class PostRequestFactory {
    
    public static List<NameValuePair> convertHashMapToNameValuePairsList(final HashMap<String, String> params)
    {
        final ArrayList<NameValuePair> namedValuePairs = new ArrayList<NameValuePair>();
        for (final String paramName : params.keySet())
            {
                namedValuePairs.add(new BasicNameValuePair(paramName, params.get(paramName)));
            }
        return namedValuePairs;

    }
    public HttpPost buildPostForSoap(final String path, final String soapAction, final String xml) throws BuildPostException
        {
            final HttpPost httppost = new HttpPost(path);
            httppost.setHeader(new BasicHeader("Content-Type", "text/xml;charset=UTF-8"));
            httppost.setHeader(new BasicHeader("SOAPAction", soapAction));
            StringEntity entity;
            try
                {
                    entity = new StringEntity(xml, "UTF-8");
                }
            catch (final UnsupportedEncodingException e)
                {
                    throw new BuildPostException(e);
                }
            httppost.setEntity(entity);
            return httppost;
        }

    public HttpPost buildPost(final String path, final HashMap<String,String> formparams) throws BuildPostException
        {
            return buildPost(path, convertHashMapToNameValuePairsList(formparams));
        }
    
    public HttpPost buildPost(final String path, final List<NameValuePair> formparams) throws BuildPostException
        {
            UrlEncodedFormEntity entity1;
            try
                {
                    entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
                }
            catch (final UnsupportedEncodingException e)
                {
                    throw new BuildPostException(e);
                }
            final HttpPost httppost = new HttpPost(path);
            httppost.setEntity(entity1);
            return httppost;
        }

    public HttpPost buildPost(final String path, final String content) throws BuildPostException
        {
            StringEntity entity1;
            try
                {
                    entity1 = new StringEntity(content);
                }
            catch (final UnsupportedEncodingException e)
                {
                    throw new BuildPostException(e);
                }
            final HttpPost httppost = new HttpPost(path);
            httppost.setEntity(entity1);
            return httppost;
        }
    
    public  <T extends HttpEntityEnclosingRequestBase> T buildRequest(Class<T> requestClass, final String path, final List<NameValuePair> formparams) throws BuildPostException
    {
        UrlEncodedFormEntity entity1;
        try
            {
                entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
            }
        catch (final UnsupportedEncodingException e)
            {
                throw new BuildPostException(e);
            }
        T request;
        try
            {
                request = requestClass.newInstance();
            }
        catch (Exception e)
            {
                throw new RuntimeException("could not create request", e);
            }
        request.setURI(URI.create(path));
        request.setEntity(entity1);
        
        return request;
    }

}
