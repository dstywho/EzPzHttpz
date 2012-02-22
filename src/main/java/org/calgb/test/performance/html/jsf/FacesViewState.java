package org.calgb.test.performance.html.jsf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FacesViewState {

    private static final String JAVAX_FACES_VIEW_STATE = "javax.faces.ViewState";

    public String getViewState(String html){
        Document doc = Jsoup.parse(html);
        return doc.getElementById(JAVAX_FACES_VIEW_STATE).attr("value");
    }
}
