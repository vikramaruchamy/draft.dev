package com.draft.htmlunit.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StaticSiteScraperTest {
    @Test
    void testStaticScraper() {
        StaticSiteScraper scraper = new StaticSiteScraper();

        scraper.createWebClient();

        String webPageURl = "https://en.wikipedia.org/wiki/HtmlUnit";

        //HtmlPage page = scraper.fillFormAndSubmit(webPageURl);
        Assertions.assertEquals("HtmlUnit - Wikipedia", scraper.readTitle(webPageURl));

        String h2Xpath = "/html/body/div[1]/div/div[3]/main/div[2]/div[3]/div[1]/h2";
        Assertions.assertEquals("Benefits[edit]", scraper.readHeadings(webPageURl,h2Xpath));


        scraper.closeWebClient();
    }

}