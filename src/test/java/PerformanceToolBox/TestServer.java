package PerformanceToolBox;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class TestServer {
    public static final String MY_RESPONSE = "MY RESPONSE";
    private int          port;
    private ServerSocket serverSocket;
    private HttpServer   server;

    
    public TestServer(int port){
        this.port = port;
    }
    public class GetHandler implements HttpHandler {


        public void handle(HttpExchange t) throws IOException
            {
                InputStream is = t.getRequestBody();
                String response = MY_RESPONSE;
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
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

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
        {
            TestServer server = new TestServer(5555);
            server.start();
            

        }

}
