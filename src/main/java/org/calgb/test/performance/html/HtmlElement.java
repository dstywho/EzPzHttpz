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

    public String getInnerHtml()
        {
            return innerHtml;
        }

    public List<HtmlElement> find(String tagName)
        {
            Vector<RegexMatch> elems = getRegExpMatchesByTagName(tagName);
            return buildHtmlElements(tagName, elems);
        }

    private Vector<RegexMatch> getRegExpMatchesByTagName(String tagName)
        {
            Vector<RegexMatch> elems = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + ".*?>.*?<\\/" + tagName + ">");
            return elems;
        }

    public List<HtmlElement> find(final String tagName, final String attribute, final String value)
        {
            final Vector<RegexMatch> matches = new RegexMatch(innerHtml.replaceAll("\\n|\\r", "")).find("<" + tagName + "[^>]+" + attribute + "=" + "\"" + value + "\""
                    + "[^>]*>");
            return buildHtmlElements(tagName, matches);
        }

    private List<HtmlElement> buildHtmlElements(final String tagName, final Vector<RegexMatch> matches)
        {
            List<HtmlElement> elementList = new ArrayList<HtmlElement>();
            for (final RegexMatch match : matches)
                {
                    elementList.add(new HtmlElement(tagName, match.getText()));
                }
            return elementList;
        }
}