package PerformanceToolBox;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleServer {

    private static final Logger LOG         = LoggerFactory.getLogger(SimpleServer.class);
    public static final String  MY_RESPONSE = "MY RESPONSE";
    private final int           port;
    private ServerSocket        serverSocket;
    private HttpServer          server;

    public SimpleServer(final int port)
        {
            this.port = port;
        }

    public class GetHandler implements HttpHandler {

        public void handle(final HttpExchange t) throws IOException
            {
                t.getRequestBody();
                final String response = MY_RESPONSE;
                t.sendResponseHeaders(200, response.length());
                final OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

    }

    public void start() throws IOException
        {
            server = HttpServer.create(new InetSocketAddress(port), port);
            server.createContext("/", new GetHandler());
            server.setExecutor(null); // creates a default executor

            server.start();

        }

    public void stop()
        {
            server.stop(0);
        }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException
        {
            final SimpleServer server = new SimpleServer(5555);
            server.start();

        }

}
