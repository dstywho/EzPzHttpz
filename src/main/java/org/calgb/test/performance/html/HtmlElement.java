package org.calgb.test.performance.html;

import java.util.HashMap;
import java.util.List;

public class HtmlElement {
    private final String tagName;
    private final String html;

    public HtmlElement(final String html)
        {
            tagName = "html";
            this.html = html;
        }

    public HtmlElement(final String tagname, final String innerHtml)
        {
            tagName = tagname;
            html = innerHtml;
        }

    public HashMap<String, String> getAttr()
        {
            final RegexMatch matcher = new RegexMatch(html);
            final String openTag = getOpenningTag(matcher);
            return getAttributesFromTag(openTag);
        }

    private HashMap<String, String> getAttributesFromTag(final String openTag)
        {
            final String attributesOnly = openTag.replaceAll("(<" + tagName + ")|(\\/*>$)|\\\"", "");
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

    private String getOpenningTag(final RegexMatch matcher)
        {
            final String openTag = matcher.find("<" + tagName + ".*?>").get(0).getText();
            return openTag;
        }

    public String getHtml()
        {
            return html;
        }

    public HtmlElementList find(final String tagName)
        {
            final List<RegexMatch> elems = getRegExpMatchesByTagName(tagName);
            return buildHtmlElements(tagName, elems);
        }

    public HtmlElement findById(final String tagName, final String id)
        {
            return find(tagName, "id", id).get(0);
        }

    private List<RegexMatch> getRegExpMatchesByTagName(final String tagName)
        {
            final List<RegexMatch> elems = new RegexMatch(html.replaceAll("\\n|\\r", "")).find("<" + tagName + ".*?>.*?<\\/" + tagName + ">");
            return elems;
        }

    public List<HtmlElement> find(final String tagName, final String attribute, final String value)
        {
            final List<RegexMatch> matches = new RegexMatch(html.replaceAll("\\n|\\r", "")).find("<" + tagName + "[^>]+" + attribute + "=" + "\"" + value + "\"" + "[^>]*>");
            return buildHtmlElements(tagName, matches);
        }

    private HtmlElementList buildHtmlElements(final String tagName, final List<RegexMatch> matches)
        {
            final HtmlElementList elementList = new HtmlElementList();
            for (final RegexMatch match : matches)
                {
                    elementList.add(new HtmlElement(tagName, match.getText()));
                }
            return elementList;
        }
}
