package net.optionfactory.minispring.blacklist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUrlParser implements UrlParser {

    private static final String urlPatternString = "(http(?:s)?)://(.+)(?:/.+)?";
    private Pattern urlPattern;

    @Override
    public String getScheme(String url) {
        final Matcher matcher = urlPattern.matcher(url);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid url: " + url);
        }
        return matcher.group(1);

    }

    @Override
    public String getHost(String url) {
        final Matcher matcher = urlPattern.matcher(url);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid url: " + url);
        }
        return matcher.group(2);
    }

    public void init() {
        urlPattern = Pattern.compile(urlPatternString);
    }
}
