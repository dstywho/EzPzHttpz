package org.calgb.test.performance;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.calgb.test.performance.html.HtmlContent;
import org.calgb.test.performance.html.HtmlParse;
import org.calgb.test.performance.httpclient.NTLMSchemeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpSession
{
    private static final Logger LOG = LoggerFactory.getLogger(HttpSession.class);
    private DefaultHttpClient httpclient;
    private BasicHttpContext context;
    private HttpHost target;
    private BasicCookieStore cookieStore;
    private int lastResponseCode;
    private String lastResponseBody;
    private HttpResponse lastResponse;
    private String lastResponseMessage;
    enum HttpProtocol{ HTTP, HTTPS}

    public HttpSession(String serverAddress, int port, HttpProtocol protocol)
    {
        init(serverAddress, port,protocol);
    }

    public int getLastResponseCode()
    {
        // return lastResponse.getStatusLine().getStatusCode();
        return lastResponseCode;
    }

    public String getLastResponseBody() throws ParseException, IOException
    {
        // return EntityUtils.toString(lastResponse.getEntity());
        return lastResponseBody;
    }

    public String getLastResponseMessage() throws ParseException, IOException
    {
        // return EntityUtils.toString(lastResponse.getEntity());
        return lastResponseMessage;
    }

    public HttpResponse executePost(String path, List<NameValuePair> formparams) throws ClientProtocolException, IOException
    {
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(path);
        httppost.setEntity(entity1);
        lastResponse = httpclient.execute(target, httppost, context);
        setLastResponseCodeAndText(lastResponse);
        printTransaction("POST", path);
        return lastResponse;
    }
    public HttpResponse executeSoapPost(String path,String soapAction, String xml) throws ClientProtocolException, IOException
    {
        HttpPost httppost = new HttpPost(path);
        httppost.setHeader(new BasicHeader("Content-Type", "text/xml;charset=UTF-8"));
        httppost.setHeader(new BasicHeader("SOAPAction", soapAction));
        StringEntity entity = new StringEntity(xml, "UTF-8");
        httppost.setEntity(entity);
        lastResponse = httpclient.execute(target, httppost, context);
        setLastResponseCodeAndText(lastResponse);
        printTransaction("POST", path);
        return lastResponse;
    }

    public HttpResponse executeGet(String path) throws ClientProtocolException, IOException
    {
        HttpGet httpget = new HttpGet(path);
        lastResponse = httpclient.execute(target, httpget, context);
        setLastResponseCodeAndText(lastResponse);
        printTransaction("GET", path);
        return lastResponse;
    }

    public void printTransaction(String method, String path) throws IOException
    {
        LOG.debug(method + " " + path + " Response:" + lastResponseCode + ", " + lastResponseMessage);
    }

    public void releaseConnectionIfAvailiable() throws IOException
    {
        if (lastResponse != null)
            lastResponse.getEntity().consumeContent();
    }

    public void setLastResponseCodeAndText(HttpResponse lastResponse) throws ParseException, IOException
    {
        lastResponseCode = lastResponse.getStatusLine().getStatusCode();
        lastResponseBody = EntityUtils.toString(lastResponse.getEntity());
        lastResponseMessage = lastResponse.getStatusLine().getReasonPhrase();
    }

    public void init(String serverAddress, int port, HttpProtocol protocol)
    {
        httpclient = new DefaultHttpClient();

        // set credentials
//        NTCredentials creds = new NTCredentials("qyz20756", "test321", "RTPWL07D10773.us1auth.corpnet1.com", "us1auth.corpnet1.com");
//        httpclient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
//        httpclient.getCredentialsProvider().setCredentials(new AuthScope("rtpus049.corpnet2.com", -1), creds);

        // create context
        context = new BasicHttpContext();
        cookieStore = new BasicCookieStore();
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        // create target
        target = new HttpHost(serverAddress, port, "http");
        if(protocol == HttpProtocol.HTTPS){
              httpclient = useTrustingTrustManager(httpclient);
        }
    }
    public DefaultHttpClient useTrustingTrustManager(DefaultHttpClient httpClient)
        {
               try
               {
                // First create a trust manager that won't care.
                X509TrustManager trustManager = new X509TrustManager()
                {
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
                    {
                        // Don't do anything.
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
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
                SSLContext sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[] { trustManager }, null);

                // Use the above SSLContext to create your socket factory
                // (I found trying to extend the factory a bit difficult due to a
                // call to createSocket with no arguments, a method which doesn't
                // exist anywhere I can find, but hey-ho).
                SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                // If you want a thread safe client, use the ThreadSafeConManager, but
                // otherwise just grab the one from the current client, and get hold of its
                // schema registry. THIS IS THE KEY THING.
                ClientConnectionManager ccm = httpClient.getConnectionManager();
                SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();

                // Register our new socket factory with the typical SSL port and the
                // correct protocol name.
                schemeRegistry.register(new Scheme("https", sf, 443));

                // Finally, apply the ClientConnectionManager to the Http Client
                // or, as in this example, create a new one.
                return new DefaultHttpClient(ccm, httpClient.getParams());
            }
            catch(Throwable t)
            {
                // AND NEVER EVER EVER DO THIS, IT IS LAZY AND ALMOST ALWAYS WRONG!
                t.printStackTrace();
                return null;
            }
        }


    public void printCookies()
    {
        System.out.println("PRINT COOKIES");
        for (Cookie cookie : cookieStore.getCookies())
        {
            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());
        }
    }

    public static void main(String[] args) throws ClientProtocolException, IOException
    {
//        PropertyConfigurator.configure("src/log4j.properties");

        HttpSession client = new HttpSession("calgbvsdev4", 80, HttpProtocol.HTTPS);
        HttpResponse response = client.executeGet("/Open/services/RandoNode?wsdl");
        System.out.println(client.getLastResponseMessage());
        System.out.println(client.getLastResponseBody());

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><getPatientData xmlns=\"urn:node:open:ctsu:westat:com\"><openRequest xsi:type=\"ns1:OpenRequest\" xmlns:ns1=\"urn:node:open:ctsu:westat:com\"><ns1:header xsi:type=\"ns1:OpenTxHeader\"><ns1:isTest xsi:type=\"xsd:boolean\">false</ns1:isTest><ns1:otherValues xsi:type=\"xsd:string\">NULL</ns1:otherValues><ns1:sourceComponent xsi:type=\"xsd:string\">OPEN Portal</ns1:sourceComponent><ns1:targetGroup xsi:type=\"xsd:string\">CALGB</ns1:targetGroup><ns1:timeStamp xsi:type=\"xsd:dateTime\">2010-07-01T14:33:45.769Z</ns1:timeStamp><ns1:txGUID xsi:type=\"xsd:string\">OPEN-TST-100701-1060021</ns1:txGUID><ns1:txType xsi:type=\"xsd:string\">PatientDataRetrival</ns1:txType></ns1:header><ns1:operation xsi:type=\"xsd:string\">POPULATE_DEMOGRAPHY_DATA</ns1:operation><ns1:otherValues xsi:type=\"xsd:string\">not applicable</ns1:otherValues><ns1:targetURL xsi:type=\"xsd:string\">https://www.test.calgbapps.org/Open/services/RandoNode?wsdl</ns1:targetURL></openRequest><openRegistration xsi:type=\"ns2:OpenRegistration\" xmlns:ns2=\"urn:node:open:ctsu:westat:com\"><ns2:ccopAccrual xsi:type=\"xsd:string\">NO</ns2:ccopAccrual><ns2:creditRecipient xsi:type=\"xsd:string\">CALGB</ns2:creditRecipient><ns2:drugShipInvCtepId xsi:type=\"xsd:string\">NULL</ns2:drugShipInvCtepId><ns2:eligibility xsi:type=\"xsd:string\">NULL</ns2:eligibility><ns2:ineligibilityReason xsi:type=\"xsd:string\">NULL</ns2:ineligibilityReason><ns2:previousTrackingNbr xsi:type=\"xsd:long\">-99999999</ns2:previousTrackingNbr><ns2:otherPmtGroup xsi:type=\"xsd:string\">NULL</ns2:otherPmtGroup><ns2:otherValues xsi:type=\"xsd:string\">NULL</ns2:otherValues><ns2:patientId xsi:type=\"xsd:string\">121342</ns2:patientId><ns2:protocolNbr xsi:type=\"xsd:string\">CALGB-140503</ns2:protocolNbr><ns2:randomizedDate xsi:type=\"xsd:dateTime\">2010-07-01T14:33:45.722Z</ns2:randomizedDate><ns2:regSiteCtepId xsi:type=\"xsd:string\">NC010</ns2:regSiteCtepId><ns2:registrarCtepId xsi:type=\"xsd:string\">20431</ns2:registrarCtepId><ns2:registrarEmail xsi:type=\"xsd:string\">russell.anderson@duke.edu</ns2:registrarEmail><ns2:reponsibleInvCtepId xsi:type=\"xsd:string\">17873</ns2:reponsibleInvCtepId><ns2:siteInstructions xsi:type=\"xsd:string\">NULL</ns2:siteInstructions><ns2:status xsi:type=\"xsd:string\">NULL</ns2:status><ns2:statusDetailText xsi:type=\"xsd:string\">NULL</ns2:statusDetailText><ns2:statusText xsi:type=\"xsd:string\">NULL</ns2:statusText><ns2:step xsi:type=\"xsd:string\">1</ns2:step><ns2:stratification xsi:type=\"xsd:string\">NULL</ns2:stratification><ns2:trackingNbr xsi:type=\"xsd:long\">70685</ns2:trackingNbr><ns2:treatingInvCtepId xsi:type=\"xsd:string\">42141</ns2:treatingInvCtepId><ns2:treatmentAssignment xsi:type=\"xsd:string\">NULL</ns2:treatmentAssignment><ns2:courierName xsi:type=\"xsd:string\">NULL</ns2:courierName><ns2:courierNbr xsi:type=\"xsd:string\">NULL</ns2:courierNbr><ns2:creditingInvCtepId xsi:type=\"xsd:string\">17873</ns2:creditingInvCtepId><ns2:userResponse xsi:type=\"xsd:string\">PT_NOT_VALIDATED</ns2:userResponse><ns2:patientStatus xsi:type=\"xsd:string\">NOT_APPLICABLE</ns2:patientStatus><ns2:offStudyReason xsi:type=\"xsd:long\">-99999999</ns2:offStudyReason><ns2:groupProtocolNumber xsi:type=\"xsd:string\">NULL</ns2:groupProtocolNumber><ns2:credentialingExceptionCode xsi:type=\"xsd:string\">NULL</ns2:credentialingExceptionCode><ns2:credentialingExceptionReason xsi:type=\"xsd:string\">NULL</ns2:credentialingExceptionReason><ns2:caseNotes xsi:type=\"ns2:string\" xsi:nil=\"true\"/><ns2:action xsi:type=\"xsd:string\">PROCESS</ns2:action></openRegistration></getPatientData></soapenv:Body></soapenv:Envelope>";
        client.executeSoapPost("/Open/services/RandoNode?wsdl", "urn:node:open:ctsu:westat:com/getPatientData", xml);
        System.out.println(client.getLastResponseBody());
//        HttpSession client = new HttpSession("blah", 4201);
//        client.executeGet("/icds/Login.action?_eventName=display");
//
//        HtmlContent form = new HtmlParse(client.getLastResponseBody()).getForms().get(1);
//        String counterSourcePage = form.find("input", "name", "_sourcePage").getAttribute("value");
//        String counterFp = form.find("input", "name", "__fp").getAttribute("value");
//
//        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        formparams.add(new BasicNameValuePair("targetUrl", ""));
//        formparams.add(new BasicNameValuePair("loginExternal", "Login"));
//        formparams.add(new BasicNameValuePair("_sourcePage", counterSourcePage));
//        formparams.add(new BasicNameValuePair("__fp", counterFp));
//        formparams.add(new BasicNameValuePair("username", "qyz20756"));
//        formparams.add(new BasicNameValuePair("password", "test321"));
//        client.executePost("/icds/Login.action", formparams);
//
//        client.executeGet("/icds/Home.action");
//
//        client.executeGet("/icds/Dictionary.action?dictionary=M");
//        form = new HtmlParse(client.getLastResponseBody()).getForms().get(1);
//        counterSourcePage = form.find("input", "name", "_sourcePage").getAttribute("value");
//        counterFp = form.find("input", "name", "__fp").getAttribute("value");
//
//        formparams = new ArrayList<NameValuePair>();
//        formparams.add(new BasicNameValuePair("_sourcePage", counterSourcePage));
//        formparams.add(new BasicNameValuePair("__fp", counterFp));
//        formparams.add(new BasicNameValuePair("dictionary", "M"));
//        formparams.add(new BasicNameValuePair("level", "105"));
//        formparams.add(new BasicNameValuePair("fields[0]", "3"));
//        formparams.add(new BasicNameValuePair("operators[0]", "13"));
//        formparams.add(new BasicNameValuePair("values[0]", "sinus"));
//        client.executePost("/icds/widgets/DictionarySearch.action?_eventName=search", formparams);
//        System.out.println(client.lastResponseBody);
//        client.releaseConnectionIfAvailiable();
    }
}
