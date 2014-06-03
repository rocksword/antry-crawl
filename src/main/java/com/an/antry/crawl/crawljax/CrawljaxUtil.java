package com.an.antry.crawl.crawljax;

import java.net.URL;

public class CrawljaxUtil {
    public static void setupDriver() {
        URL url = CrawljaxUtil.class.getResource("/driver/chromedriver.exe");
        System.out.println(url.toString());
        System.getProperties().setProperty("webdriver.chrome.driver", url.getPath());
    }
}
