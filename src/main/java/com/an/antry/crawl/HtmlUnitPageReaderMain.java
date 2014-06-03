package com.an.antry.crawl;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

public class HtmlUnitPageReaderMain {
    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        analyze();
    }

    private static void analyze() throws IOException, MalformedURLException {
        String url = "http://dl.pconline.com.cn/download/90675-1.html";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        JavaScriptEngine eng = webClient.getJavaScriptEngine();
        JavaScriptConfiguration conf = eng.getJavaScriptConfiguration();
        HtmlUnitContextFactory fac = eng.getContextFactory();
        webClient.setJavaScriptEngine(new JavaScriptEngine(webClient));
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        System.out.println(page.asText());
    }
}
