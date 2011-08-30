package PerformanceToolBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.calgb.test.performance.ProcessResponseBodyException;
import org.calgb.test.performance.SimplifiedResponse;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class SimplifiedResponseTest {
    private static final String RANDOM_STRING = "randomString";
    private static final String UTF_8         = "UTF-8";
    private static final String HTML_HTML     = "<html></html>";
    private static final String SERVER_ERROR  = "SERVER ERROR";

    @Test
    public void test() throws ParseException, IOException, ProcessResponseBodyException
        {
            final HttpResponse mockHttpResponse = EasyMock.createMock(HttpResponse.class);
            final org.apache.http.StatusLine mockStatusLine = EasyMock.createMock(StatusLine.class);
            final HttpEntity mockHttpEntity = EasyMock.createMock(HttpEntity.class);

            EasyMock.expect(mockHttpResponse.getStatusLine()).andReturn(mockStatusLine);
            EasyMock.expect(mockStatusLine.getStatusCode()).andReturn(500).anyTimes();
            EasyMock.expect(mockStatusLine.getReasonPhrase()).andReturn(SERVER_ERROR).anyTimes();
            EasyMock.expect(mockHttpResponse.getEntity()).andReturn(mockHttpEntity);
            final String html = new String(HTML_HTML);
            final ByteArrayInputStream htmlInputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
            EasyMock.expect(mockHttpEntity.getContent()).andReturn(htmlInputStream);
            EasyMock.expect(mockHttpEntity.getContentLength()).andReturn(html.length() + 0L).anyTimes();
            EasyMock.expect(mockHttpEntity.getContentType()).andReturn(new BasicHeader(RANDOM_STRING, RANDOM_STRING)).anyTimes();

            EasyMock.replay(mockHttpEntity, mockHttpResponse, mockStatusLine);
            final SimplifiedResponse simpResponse = new SimplifiedResponse(mockHttpResponse);

            EasyMock.verify(mockHttpEntity, mockHttpResponse, mockStatusLine);
            Assert.assertEquals(mockStatusLine.getStatusCode(), simpResponse.getCode());
            Assert.assertEquals(mockStatusLine.getReasonPhrase(), simpResponse.getMessage());
            Assert.assertEquals(html, simpResponse.getBody());

        }
}
