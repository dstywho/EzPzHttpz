package PerformanceToolBox;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.calgb.test.performance.HttpSession;
import org.calgb.test.performance.HttpSession.HttpProtocol;
import org.calgb.test.performance.ProcessResponseBodyException;
import org.calgb.test.performance.RequestException;
import org.calgb.test.performance.SimplifiedResponse;
import org.calgb.test.performance.UseSslException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSessionTest {
    private static final Logger LOG  = LoggerFactory.getLogger(HttpSessionTest.class);
    public static int           PORT = 5555;

    static class ServerThread extends Thread {

        private final int port;

        public ServerThread(final int port)
            {
                this.port = port;
            }

        private SimpleServer server;

        @Override
        public void run()
            {
                server = new SimpleServer(port);
                try
                    {
                        server.start();
                    }
                catch (final IOException e)
                    {
                        e.printStackTrace();
                    }
            }

        public void stopServer()
            {
                server.stop();
                stop();
            }
    }

    private static ServerThread serverThread;

    @BeforeClass
    public static void setup()
        {

            serverThread = new ServerThread(PORT);
            serverThread.start();
        }

    @AfterClass
    public static void teardown() throws InterruptedException
        {
            serverThread.stopServer();
        }

    @Test
    public void getRequestSpec() throws UseSslException, RequestException, ProcessResponseBodyException
        {
            final HttpSession session = new HttpSession("localhost", PORT, HttpProtocol.HTTP);
            final SimplifiedResponse response = session.executeGet("/");
            LOG.debug(response.getBody());
            assertEquals(SimpleServer.MY_RESPONSE, response.getBody());
        }
//
//    @Test
//    public void postRequestSpec() throws SessionStartException, RequestException, ResponseParseException, KeyManagementException, NoSuchAlgorithmException,
//            ClientProtocolException, IOException, UseSslException, BuildPostException, ProcessResponseBodyException
//        {
//            final HttpSession session = new HttpSession("localhost", PORT, HttpProtocol.HTTP);
//            final List<NameValuePair> params = new ArrayList<NameValuePair>()
//                {
//                    /**
//                 * 
//                 */
//                    private static final long serialVersionUID = 1L;
//
//                        {
//                            add(new BasicNameValuePair("blah", "val2"));
//                        }
//                };
//            final SimplifiedResponse response = session.executePost("/", params);
//
//            assertEquals(SimpleServer.MY_RESPONSE, response.getBody());
//        }
}
