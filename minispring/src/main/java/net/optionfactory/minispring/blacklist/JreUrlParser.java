package net.optionfactory.minispring.blacklist;

import java.net.MalformedURLException;
import java.net.URL;

public class JreUrlParser implements UrlParser {

    @Override
    public String getHost(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getHost();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public String getScheme(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getProtocol();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
