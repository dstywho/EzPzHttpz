package org.calgb.test.performance.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class NewHtmlElement {
    private String tagName;
    private String innerHtml;

    public NewHtmlElement(final String tagname, final String innerHtml)
        {
            this.tagName = tagname;
            this.innerHtml = innerHtml;
        }

    public HashMap<String, String> getAttr()
        {
            RegexMatch matcher = new RegexMatch(innerHtml);
            String openTag = matcher.find("<" + tagName + ".*?>").get(0).getText();
            String attributesOnly = openTag.replaceAll("(<" + tagName + ")|(\\/*>$)|\\\"", "");
            final String[] tagPairs = attributesOnly.split("\\s");

            final HashMap<String, String> attrPairs = new HashMap<String, String>();
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

    public List<NewHtmlElement> find(String tagName)
        {
            Vector<RegexMatch> elems = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + ".*?>.*?<\\/" + tagName + ">");
            return buildHtmlElements(tagName, elems);
        }

    public List<NewHtmlElement> find(final String tagName, final String attribute, final String value)
        {
            final Vector<RegexMatch> matches = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + "[^>]+" + attribute + "=" + "\"" + value + "\""
                    + "[^>]*>");
            return buildHtmlElements(tagName, matches);
        }

    private List<NewHtmlElement> buildHtmlElements(final String tagName, final Vector<RegexMatch> matches)
        {
            List<NewHtmlElement> elementList = new ArrayList<NewHtmlElement>();
            for (final RegexMatch match : matches)
                {
                    elementList.add(new NewHtmlElement(tagName, match.getText()));
                }
            return elementList;
        }
}
