package PerformanceToolBox;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.calgb.test.performance.HttpSession;
import org.calgb.test.performance.SimplifiedResponse;
import org.calgb.test.performance.HttpSession.HttpProtocol;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpSessionTest {
   public static int PORT = 5555;
    
    class ServerThread extends Thread{
        public void run(){
            TestServer server = new TestServer(PORT);
            try
                {
                    server.start();
                }
            catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }
    private ServerThread serverThread;

    @Before
    public void setup(){
        serverThread = new ServerThread();
        serverThread.start();
    }
    @After
    public void teardown(){
        serverThread.stop();
    }
    
    @Test
    public void getRequestSpec() throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException
        {
            HttpSession session = new HttpSession("localhost",PORT, HttpProtocol.HTTP);
            SimplifiedResponse response = session.executeGet("/");
            assertEquals(TestServer.MY_RESPONSE,response.getBody());
        }
    @Test
    public void postRequestSpec() throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException
        {
            HttpSession session = new HttpSession("localhost",PORT, HttpProtocol.HTTP);
            List<NameValuePair> params = new ArrayList<NameValuePair>(){{add(new BasicNameValuePair("blah", "val2"));}};
            SimplifiedResponse response = session.executePost("/", params);
            assertEquals(TestServer.MY_RESPONSE,response.getBody());
        }
}
