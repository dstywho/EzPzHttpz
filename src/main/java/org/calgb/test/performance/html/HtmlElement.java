package org.calgb.test.performance.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class HtmlElement {
    private String tagName;
    private String innerHtml;

    public HtmlElement(final String tagname, final String innerHtml)
        {
            this.tagName = tagname;
            this.innerHtml = innerHtml;
        }

    public HashMap<String, String> getAttr()
        {
            RegexMatch matcher = new RegexMatch(innerHtml);
            String openTag = getOpenningTag(matcher);
            return getAttributesFromTag(openTag);
        }

    private HashMap<String, String> getAttributesFromTag(String openTag)
        {
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

    private String getOpenningTag(RegexMatch matcher)
        {
            String openTag = matcher.find("<" + tagName + ".*?>").get(0).getText();
            return openTag;
        }

    public String getHtml()
        {
            return innerHtml;
        }

    public HtmlElementList find(String tagName)
        {
            List<RegexMatch> elems = getRegExpMatchesByTagName(tagName);
            return buildHtmlElements(tagName, elems);
        }

    private List<RegexMatch> getRegExpMatchesByTagName(String tagName)
        {
            List<RegexMatch> elems = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + ".*?>.*?<\\/" + tagName + ">");
            return elems;
        }

    public List<HtmlElement> find(final String tagName, final String attribute, final String value)
        {
            final List<RegexMatch> matches = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + "[^>]+" + attribute + "=" + "\"" + value + "\""
                    + "[^>]*>");
            return buildHtmlElements(tagName, matches);
        }

    private HtmlElementList buildHtmlElements(final String tagName, final List<RegexMatch> matches)
        {
            HtmlElementList elementList = new HtmlElementList();
            for (final RegexMatch match : matches)
                {
                    elementList.add(new HtmlElement(tagName, match.getText()));
                }
            return elementList;
        }
}
