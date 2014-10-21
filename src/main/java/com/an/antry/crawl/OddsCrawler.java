package com.an.antry.crawl;

import java.net.MalformedURLException;
import java.net.URL;

public class OddsCrawler {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://fenxi.zgzcw.com/export/1765699/bjop");
            DownloadThread thread = new DownloadThread(url, "1765699_bjop_c.xls");
            thread.run();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void readTurn() throws MalformedURLException {
        String urlStr = "http://saishi.zgzcw.com/soccer/league/36/2013-2014";
        String pageContent = WebPageReader.readPageByJavaApi(urlStr);
        System.out.println("Read page:");
        System.out.println(pageContent);
    }

    static void readOdds() throws MalformedURLException {
        String urlStr = "http://fenxi.zgzcw.com/1765699/bjop";
        String pageContent = WebPageReader.readPageByJavaApi(urlStr);
        System.out.println(pageContent);
    }

    static void readExcel() throws MalformedURLException {
        String urlStr = "http://fenxi.zgzcw.com/export/1765699/bjop";
        String pageContent = WebPageReader.readPageByJavaApi(urlStr);
        System.out.println(pageContent);
    }
}
