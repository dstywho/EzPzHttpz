package org.calgb.test.performance.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.DefaultedHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.calgb.test.performance.html.HtmlContent;
import org.calgb.test.performance.html.HtmlParse;
import org.calgb.test.performance.html.RegexMatch;


import sun.net.www.http.HttpClient;


public class HttpClientDemo {
	
	public static void getAttributes(String text){
		String inputTag = text.replaceAll("<input|\\/?>$", "");
		for(RegexMatch attributeMatch : new RegexMatch(inputTag).find("(\\w)+=\"(\\w|\\s)+\"")){
			String attributeStr = attributeMatch.getText();
			attributeStr = attributeStr.replaceAll("=", "");
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
///////////////////////////SETUP STUFF/////////////////////////
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		NTCredentials creds = new NTCredentials("qyz20756", "test321", "RTPWL07D10773.us1auth.corpnet1.com", "us1auth.corpnet1.com");
		httpclient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
		httpclient.getCredentialsProvider().setCredentials(new AuthScope("rtpus049.corpnet2.com",-1), creds);
		
		HttpContext context = new BasicHttpContext();
		CookieStore cookieStore = new BasicCookieStore();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
//////////////////////////END/////////////////////////////////////////
		
//////////////////////GET///////////////////////////////////////
		HttpHost target = new HttpHost("rtpus049.corpnet2.com", 4201, "http");
		HttpGet httpget = new HttpGet("/icds/Login.action?_eventName=display");
		HttpResponse response = httpclient.execute(target, httpget, context);
		
		
		System.out.println(response.getStatusLine().getStatusCode());
		String html = EntityUtils.toString(response.getEntity());
		
	
		//grab some counterfeit protection stuff
		HtmlContent form = new HtmlParse(html).getForms().get(1);
		String counterSourcePage = form.find("input", "name","_sourcePage").getAttribute("value");
		String counterFp = form.find("input", "name","__fp").getAttribute("value");
		System.out.println("sourcePage:" +  counterSourcePage);
		System.out.println("fp:" +  counterFp);
///////////////////////END/////////////////////////////////////

		
/////////////////////////POST/////////////////////////////////
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("targetUrl", ""));
		formparams.add(new BasicNameValuePair("loginExternal", "Login"));
		formparams.add(new BasicNameValuePair("_sourcePage", counterSourcePage));
		formparams.add(new BasicNameValuePair("__fp", counterFp));
		formparams.add(new BasicNameValuePair("username", "qyz20756"));
		formparams.add(new BasicNameValuePair("password", "test321"));
		UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
		HttpPost httppost = new HttpPost("/icds/Login.action");
		httppost.setEntity(entity1);
		response = httpclient.execute(target, httppost, context);
		
		html = EntityUtils.toString(response.getEntity());
		System.out.println(html);
		System.out.println(!html.contains("unexpected error occurred"));

		System.out.println(response.getStatusLine().getStatusCode());
///////////////////////////////////END//////////////////////////////////////
		
///////////////////////////////GET//////////////////////

//		target = new HttpHost("rtpus049.corpnet2.com", 4201, "http");
		httpget = new HttpGet("/icds/Home.action");
		response = httpclient.execute(target, httpget, context);
		html = EntityUtils.toString(response.getEntity());
		System.out.println(html);
/////////////////////////END/////////////////////////////

	}

	private static void assertTrue(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
