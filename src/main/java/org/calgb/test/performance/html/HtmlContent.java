package org.calgb.test.performance.html;

import java.util.HashMap;
import java.util.Vector;

public class HtmlContent {
    private HtmlElement element;
    private String      innerHtml;

    HtmlContent(final String html)
        {

        }

    HtmlContent(final HtmlElement htmlElement, final String innerHtml)
        {
            this.element = htmlElement;
            this.innerHtml = innerHtml;
        }

    public HtmlElement getElement()
        {
            return element;
        }

    HashMap<String, String> getAttributes(final RegexMatch match, final String tagName)
        {
            final HashMap<String, String> attrPairs = new HashMap<String, String>();
            String tagDeclaration = match.find("<" + tagName + ".*?>").get(0).getText();
            tagDeclaration = tagDeclaration.replaceAll("(<form)|(\\/*>$)|\\\"", "");
            final String[] tagPairs = tagDeclaration.split("\\s");
            for (final String tagPair : tagPairs)
                {
                    if (tagPair.contains("="))
                        {
                            final int eqlIdx = tagPair.indexOf("=");
                            attrPairs.put(tagPair.substring(0, eqlIdx), tagPair.substring(eqlIdx + 1));
                        }
                }
            return attrPairs;
        }

    public String getInnerHtml()
        {
            return innerHtml;
        }

    public HtmlElement find(final String tagName, final String attribute, final String value)
        {
            final Vector<RegexMatch> matches = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + "[^>]+" + attribute
                    + "=" + "\"" + value + "\"" + ".*?\\/>");

            for (final RegexMatch match : matches)
                {
                    final HashMap<String, String> attributes = getAttributes(match, tagName);
                    return new HtmlElement(attributes);
                }
            return null;
        }
}
