package org.calgb.test.performance.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatch {
    String content;

    public RegexMatch(final String content)
        {
            this.content = content;
        }

    public RegexMatches find(final String regexp)
        {
            final Matcher matcher = Pattern.compile(regexp).matcher(content);
            final RegexMatches matches = new RegexMatches();
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
