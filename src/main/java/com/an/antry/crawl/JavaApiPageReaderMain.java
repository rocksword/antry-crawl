package com.an.antry.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class JavaApiPageReaderMain {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        JavaApiPageReaderMain main = new JavaApiPageReaderMain();
        String result = main.analyze();
        System.out.println("result: " + result);
    }

    public String analyze() throws UnsupportedEncodingException, IOException {
        StringBuilder builder = new StringBuilder();
        String urlstr = "http://dl.pconline.com.cn/download/90675-1.html";
        URL url = new URL(urlstr);
        InputStreamReader in = null;
        BufferedReader br = null;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String contentType = conn.getContentType();
        System.out.println("contentType: " + contentType);
        String charset = getCharset(contentType);

        in = new InputStreamReader(conn.getInputStream(), charset);
        br = new BufferedReader(in);
        String temp;
        while ((temp = br.readLine()) != null) {
            builder.append(temp);
        }
        br.close();
        in.close();
        return builder.toString();
    }

    private String getCharset(String str) {
        if (StringUtils.isEmpty(str)) {
            return "UTF-8";
        }
        String result = null;
        Pattern pat = Pattern.compile("charset=.*");
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            result = mat.group(0).split("charset=")[1];
        }
        if (StringUtils.isEmpty(result)) {
            return "UTF-8";
        }
        return result;
    }
}
