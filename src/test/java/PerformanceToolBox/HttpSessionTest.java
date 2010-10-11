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
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSessionTest {
    private static final Logger LOG = LoggerFactory.getLogger(HttpSessionTest.class);
   public static int PORT = 5555;
    
    static class ServerThread extends Thread{
        
        private int port;

        public ServerThread(int port){
            this.port = port;
        }
        private SimpleServer server;

        public void run(){
            server = new SimpleServer(port);
            try
                {
                    server.start();
                }
            catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        
        public void stopServer(){
            server.stop();
            stop();
        }
    }
    private static ServerThread serverThread;

    @BeforeClass
    public static void setup(){
           
        serverThread = new ServerThread(PORT );
        serverThread.start();
    }
    
    @AfterClass
    public static void teardown() throws InterruptedException{
        serverThread.stopServer();
    }
    
    @Test
    public void getRequestSpec() throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException
        {
            HttpSession session = new HttpSession("localhost",PORT, HttpProtocol.HTTP);
            SimplifiedResponse response = session.executeGet("/");
            LOG.debug(response.getBody());
            assertEquals(SimpleServer.MY_RESPONSE,response.getBody());
        }
    @Test
    public void postRequestSpec() throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException
        {
            HttpSession session = new HttpSession("localhost",PORT, HttpProtocol.HTTP);
            List<NameValuePair> params = new ArrayList<NameValuePair>(){{add(new BasicNameValuePair("blah", "val2"));}};
            SimplifiedResponse response = session.executePost("/", params);
            
            assertEquals(SimpleServer.MY_RESPONSE,response.getBody());
        }
}
