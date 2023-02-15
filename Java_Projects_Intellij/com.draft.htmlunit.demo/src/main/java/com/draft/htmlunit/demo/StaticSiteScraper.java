package com.draft.htmlunit.demo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class StaticSiteScraper {

    private static WebClient webClient;

    public static void main(String[] args) {
        StaticSiteScraper scraper = new StaticSiteScraper();
        scraper.createWebClient();
        String webPageURl = "https://en.wikipedia.org/wiki/HtmlUnit";

        String pageTitle = scraper.readTitle(webPageURl);
        System.out.println(pageTitle);

        String xPath = "/html/body/div[1]/div/div[3]/main/div[2]/div[3]/div[1]/h2";
        String heading2 = scraper.readHeadings(webPageURl, xPath);
        System.out.println(heading2);
        scraper.closeWebClient();
    }

    public void createWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
    }

    public void closeWebClient() {
        webClient.close();
    }

    public String readHeadings(String webPageURl, String xPath) {

        List<HtmlHeading2> h2 = null;

        String firstH2 = null;

        try {
            HtmlPage page = webClient.getPage(webPageURl);

            h2 = (List<HtmlHeading2>)(Object) page.getByXPath(xPath);

            firstH2 = (h2.get(0)).getTextContent();

            System.out.println(firstH2);


        } catch (FailingHttpStatusCodeException | IOException e) {
            e.printStackTrace();
        }
        return firstH2;
    }

    public String readTitle(String webPageURl) {

        String pageTitle = null;

        try {

            HtmlPage page = webClient.getPage(webPageURl);

            pageTitle = page.getTitleText();

            System.out.println(pageTitle);
        } catch (FailingHttpStatusCodeException | IOException e) {
            e.printStackTrace();
        }
        return pageTitle;
    }
}
