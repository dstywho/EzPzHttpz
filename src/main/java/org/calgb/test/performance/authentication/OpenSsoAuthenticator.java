package org.calgb.test.performance.authentication;

import java.util.HashMap;

import org.calgb.test.performance.BuildPostException;
import org.calgb.test.performance.HttpSession;
import org.calgb.test.performance.ProcessResponseBodyException;
import org.calgb.test.performance.RequestException;
import org.calgb.test.performance.SimplifiedResponse;
import org.calgb.test.performance.html.Form;
import org.calgb.test.performance.html.HtmlElement;
import org.calgb.test.performance.html.HtmlElementList;

public class OpenSsoAuthenticator {
    
    private static final String SSO_UI_LOGIN_URL = "/sso/UI/Login";
    public void authenticate(HttpSession session, String username, String password) throws UnableToAuthenticateThroughSsoException 
    {
        try
            {
                authenticateWithOpenSso(session,username,password);
            }
        catch (Exception e)
            {
                throw new UnableToAuthenticateThroughSsoException(e);
            }
    }
    
    
    private void authenticateWithOpenSso(HttpSession session, String username, String password) throws RequestException, ProcessResponseBodyException, BuildPostException
    {
        Form form = getForm(session);
        String action_url = getFormAction(form);
        HashMap<String, String> params = createFormParameters(form, username, password);
        SimplifiedResponse response = session.executePost(action_url, params);
        
        
    }
    
    private Form getForm(HttpSession session) throws RequestException, ProcessResponseBodyException
    {
        SimplifiedResponse ssoGetresponse = session.executeGet(SSO_UI_LOGIN_URL);
        HtmlElementList formElements = new HtmlElement("html", ssoGetresponse.getBody()).find("form");
        Form formElem = new Form("form", formElements.get(formElements.size() - 1).getHtml());
        return formElem;
    }
    
    
    private String getFormAction(Form form)
        {
            String action_url = form.getAttr().get("action");
            action_url = action_url == null ? SSO_UI_LOGIN_URL : action_url;
            return action_url;
        }
    
    private HashMap<String, String> createFormParameters(Form form, String username, String password)
    {
        HashMap<String, String> params = form.getParameters();
        params.put("IDToken2", password);
        params.put("IDToken1", username);
        return params;
    }
    
}
