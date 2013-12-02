package org.calgb.test.performance;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

public class DeleteWithBodyFactory {
    
    public HttpDeleteWithBody create(String path, String content) throws BuildPostException{
        HttpDeleteWithBody request = new HttpDeleteWithBody(path);
        StringEntity entity;
        try
            {
                entity = new StringEntity(content, "UTF-8");
            }
        catch (final UnsupportedEncodingException e)
            {
                throw new BuildPostException(e);
            }
        request.setEntity(entity);
        return request;
        
    }

}
