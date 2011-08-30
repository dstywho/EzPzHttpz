package org.calgb.test.performance.html;

import java.util.ArrayList;

public class HtmlElementList extends ArrayList<HtmlElement> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getHtml()
        {
            final StringBuffer html = new StringBuffer();
            for (final HtmlElement elem : this)
                {
                    html.append(elem.getHtml());
                }
            return html.toString();
        }
}
