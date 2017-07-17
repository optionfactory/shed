package net.optionfactory.miniurl.blacklist;

public interface UrlParser {

    String getHost(String url);

    String getScheme(String url);

}
