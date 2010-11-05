package org.calgb.test.performance;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSession {
    private static final Logger LOG  = LoggerFactory.getLogger(HttpSession.class);
    private static final String POST = "POST";
    private DefaultHttpClient   httpclient;
    private BasicHttpContext    context;

    public BasicHttpContext getContext()
        {
            return context;
        }

    private HttpHost     target;

    /**
     * Need to store this state.
     */
    private HttpResponse lastResponse;

    public enum HttpProtocol {
        HTTP, HTTPS
    };

    public HttpSession(final String serverAddress, final int port, final HttpProtocol protocol) throws SessionStartException
        {
            try
                {
                    init(serverAddress, port, protocol);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new SessionStartException("Failed to start http session.", e);
                }
        }

    public HttpSession(final String serverAddress, final int port, final HttpProtocol protocol, BasicHttpContext context) throws SessionStartException
        {
            this.context = context;

            try
                {
                    setTarget(serverAddress, port, protocol);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new SessionStartException("Failed to start http session.", e);
                }
        }

    public SimplifiedResponse executePost(final String path, final List<NameValuePair> formparams) throws RequestException, ResponseParseException
        {
            SimplifiedResponse simplifiedResponse;
            requestPost(path, formparams);
            try
                {
                    simplifiedResponse = new SimplifiedResponse(lastResponse);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new ResponseParseException("Failed to parse post request.", e);// TODO
                    // Auto-generated
                    // catch block
                }
            LOG.debug("Request:{} {} Response: {}", new Object[] { "POST", path, simplifiedResponse.getCode() });
            return simplifiedResponse;
        }

    private void requestPost(final String path, final List<NameValuePair> formparams) throws RequestException
        {
            HttpPost httppost;
            try
                {
                    httppost = buildPost(path, formparams);
                    lastResponse = httpclient.execute(target, httppost, context);
                }
            catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                    throw new RequestException("Failed to build post request", e);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new RequestException("Failed to execute post request", e);
                }
        }

    public SimplifiedResponse executePost(final String path, final HashMap<String, String> params) throws RequestException, ResponseParseException
        {
            List<NameValuePair> formparams = convertHashMapToNameValuePairsList(params);
            return executePost(path, formparams);
        }

    private List<NameValuePair> convertHashMapToNameValuePairsList(HashMap<String, String> params)
        {
            ArrayList<NameValuePair> namedValuePairs = new ArrayList<NameValuePair>();
            for (String paramName : params.keySet())
                {
                    namedValuePairs.add(new BasicNameValuePair(paramName, params.get(paramName)));
                }
            return namedValuePairs;

        }

    public SimplifiedResponse executeSoapPost(final String path, final String soapAction, final String xml) throws RequestException, ResponseParseException
        {
            HttpResponse lastResponse = requestSoapPost(path, soapAction, xml);
            SimplifiedResponse simplifiedResponse = null;
            try
                {
                    simplifiedResponse = new SimplifiedResponse(lastResponse);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    LOG.error("unable to parse", e.toString());
                    throw new ResponseParseException("unable to parse response", e);
                }

            LOG.debug("Request:{} {} Response: {}, {}", new Object[] { "POST", path, simplifiedResponse.getCode(), simplifiedResponse.getBody() });
            return simplifiedResponse;
        }

    private HttpResponse requestSoapPost(final String path, final String soapAction, final String xml) throws RequestException
        {
            HttpResponse lastResponse = null;
            try
                {
                    HttpPost httppost = buildPostForSoap(path, soapAction, xml);
                    lastResponse = httpclient.execute(target, httppost, context);
                }
            catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                    throw new RequestException("", POST, "failed to build request", e);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new RequestException("", POST, "failed request", e);
                }
            return lastResponse;
        }

    public SimplifiedResponse executeGet(final String path) throws RequestException, ResponseParseException
        {
            HttpResponse lastResponse = requestGet(path);

            SimplifiedResponse simplifiedResponse;
            try
                {
                    simplifiedResponse = new SimplifiedResponse(lastResponse);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new ResponseParseException("Failed to parse get response", e);

                }
            LOG.debug("Request:{} {} Response: {}", new Object[] { "GET", path, simplifiedResponse.getCode() });
            return simplifiedResponse;
        }

    private HttpResponse requestGet(final String path) throws RequestException
        {
            HttpResponse lastresponse = null;
            final HttpGet httpget = buildGet(path);
            try
                {
                    lastresponse = httpclient.execute(target, httpget, context);
                }
            catch (Exception e)
                {
                    e.printStackTrace();
                    throw new RequestException("Failed get request.", e);

                }
            return lastresponse;
        }

    private HttpGet buildGet(final String path)
        {
            final HttpParams httpparams = new BasicHttpParams();
            httpparams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            final HttpGet httpget = new HttpGet(path);
            httpget.setParams(httpparams);
            return httpget;
        }

    private HttpPost buildPost(final String path, final List<NameValuePair> formparams) throws UnsupportedEncodingException
        {
            final UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
            final HttpPost httppost = new HttpPost(path);
            httppost.setEntity(entity1);
            return httppost;
        }

    private HttpPost buildPostForSoap(final String path, final String soapAction, final String xml) throws UnsupportedEncodingException
        {
            final HttpPost httppost = new HttpPost(path);
            httppost.setHeader(new BasicHeader("Content-Type", "text/xml;charset=UTF-8"));
            httppost.setHeader(new BasicHeader("SOAPAction", soapAction));
            final StringEntity entity = new StringEntity(xml, "UTF-8");
            httppost.setEntity(entity);
            return httppost;
        }

    public void releaseConnectionIfAvailiable() throws IOException
        {
            if (lastResponse != null)
                {
                    lastResponse.getEntity().consumeContent();
                }
        }

    private void init(final String serverAddress, final int port, final HttpProtocol protocol) throws SessionStartException
        {
            httpclient = new DefaultHttpClient();

            // create context
            createContext();

            // create target
            setTarget(serverAddress, port, protocol);
        }

    public void setTarget(final String serverAddress, final int port, final HttpProtocol protocol) throws SessionStartException
        {
            target = new HttpHost(serverAddress, port, "http");
            if (protocol == HttpProtocol.HTTPS)
                {
                    target = new HttpHost(serverAddress, port, "https");
                    try
                        {
                            httpclient = useTrustingTrustManager(httpclient);
                            Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
                        }
                    catch (Exception e)
                        {
                            throw new SessionStartException("trouble setting up ssl settings");
                        }
                }
        }

    private void createContext()
        {
            context = new BasicHttpContext();
            BasicCookieStore cookieStore = new BasicCookieStore();
            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }

    public void addCookie(final String domain, final String name, final String value, final String path)
        {
            final CookieStore store = (CookieStore) context.getAttribute(ClientContext.COOKIE_STORE);
            final BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
            cookie.setDomain(domain);
            cookie.setPath(path);
            store.addCookie(cookie);
            context.setAttribute(ClientContext.COOKIE_STORE, store);
        }

    private DefaultHttpClient useTrustingTrustManager(final DefaultHttpClient httpClient) throws KeyManagementException, NoSuchAlgorithmException
        {
            // First create a trust manager that won't care.
            final X509TrustManager trustManager = new X509TrustManager()
                {
                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
                        {
                            // Don't do anything.
                        }

                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
                        {
                            // Don't do anything.
                        }

                    public X509Certificate[] getAcceptedIssuers()
                        {
                            // Don't do anything.
                            return null;
                        }
                };

            // Now put the trust manager into an SSLContext.
            final SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { trustManager }, null);

            // Use the above SSLContext to create your socket factory
            // (I found trying to extend the factory a bit difficult due to a
            // call to createSocket with no arguments, a method which doesn't
            // exist anywhere I can find, but hey-ho).
            final SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // If you want a thread safe client, use the ThreadSafeConManager, but
            // otherwise just grab the one from the current client, and get hold of its
            // schema registry. THIS IS THE KEY THING.
            final ClientConnectionManager ccm = httpClient.getConnectionManager();
            final SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();

            // Register our new socket factory with the typical SSL port and the
            // correct protocol name.
            schemeRegistry.register(new Scheme("https", sf, 443));

            // Finally, apply the ClientConnectionManager to the Http Client
            // or, as in this example, create a new one.
            return new DefaultHttpClient(ccm, httpClient.getParams());
        }

}
