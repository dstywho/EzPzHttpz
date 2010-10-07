package PerformanceToolBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.calgb.test.performance.SimplifiedResponse;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class SimplifiedResponseTest {
    @Test
    public void test() throws ParseException, IOException
        {
            HttpResponse mockHttpResponse = EasyMock.createMock(HttpResponse.class);
            org.apache.http.StatusLine mockStatusLine = EasyMock.createMock(StatusLine.class);
            HttpEntity mockHttpEntity = EasyMock.createMock(HttpEntity.class);

            EasyMock.expect(mockHttpResponse.getStatusLine()).andReturn(mockStatusLine);
            EasyMock.expect(mockStatusLine.getStatusCode()).andReturn(500).anyTimes();
            EasyMock.expect(mockStatusLine.getReasonPhrase()).andReturn("SERVER ERROR").anyTimes();
            EasyMock.expect(mockHttpResponse.getEntity()).andReturn(mockHttpEntity);
            String html = new String("<html></html>");
            ByteArrayInputStream htmlInputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
            EasyMock.expect(mockHttpEntity.getContent()).andReturn(htmlInputStream);
            EasyMock.expect(mockHttpEntity.getContentLength()).andReturn(html.length() + 0L).anyTimes();
            EasyMock.expect(mockHttpEntity.getContentType()).andReturn(new BasicHeader("randomString", "anything")).anyTimes();

            EasyMock.replay(mockHttpEntity, mockHttpResponse, mockStatusLine);
            SimplifiedResponse simpResponse = new SimplifiedResponse(mockHttpResponse);
            
            EasyMock.verify(mockHttpEntity, mockHttpResponse, mockStatusLine);
            Assert.assertEquals(mockStatusLine.getStatusCode(), simpResponse.getCode());
            Assert.assertEquals(mockStatusLine.getReasonPhrase(), simpResponse.getMessage());
            Assert.assertEquals(html, simpResponse.getBody());

        }
}