package com.an.antry.crawl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebPageReader {
    private static final Log logger = LogFactory.getLog(WebPageReader.class);
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String readPageByJavaApi(String urlStr) {
        StringBuffer result = new StringBuffer();
        try {
            URL pageUrl = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) pageUrl.openConnection();
            String contentType = conn.getContentType();
            logger.info("contentType: " + contentType);
            String charset = getCharset(contentType);

            InputStreamReader in = new InputStreamReader(pageUrl.openStream(), charset);
            BufferedReader reader = new BufferedReader(in);

            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
        return result.toString();
    }

    public static String readPageByWebClient(String urlStr) {
        String result = null;
        try {
            URL pageUrl = new URL(urlStr);
            WebClient webClient = new WebClient(BrowserVersion.getDefault());
            HtmlPage page = (HtmlPage) webClient.getPage(pageUrl);
            result = page.asXml();
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
        return result;
    }

    private static String getCharset(String str) {
        if (StringUtils.isEmpty(str)) {
            return DEFAULT_CHARSET;
        }
        String result = null;
        Pattern pat = Pattern.compile("charset=.*");
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            result = mat.group(0).split("charset=")[1];
        }
        if (StringUtils.isEmpty(result)) {
            return DEFAULT_CHARSET;
        }
        return result;
    }
}
