package org.calgb.test.performance.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatch {
    String content;

    public RegexMatch(final String content)
        {
            this.content = content;
        }

    public List<RegexMatch> find(final String regexp)
        {
            final Matcher matcher = Pattern.compile(regexp).matcher(content);
            final ArrayList<RegexMatch> matches = new ArrayList<RegexMatch>();
            while (matcher.find())
                {
                    matches.add(new RegexMatch(content.substring(matcher.start(), matcher.end())));
                }
            return matches;
        }

    public String getText()
        {
            return content;
        }

}
