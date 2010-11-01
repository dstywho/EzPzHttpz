package org.calgb.test.performance.html;

import java.util.ArrayList;

public class HtmlElementList extends ArrayList<HtmlElement> {
    public String getHtml()
        {
            StringBuffer html = new StringBuffer();
            for (HtmlElement elem : this)
                {
                    html.append(elem.getHtml());
                }
            return html.toString();
        }
}
