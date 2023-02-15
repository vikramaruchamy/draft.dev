package com.draft.htmlunit.demo;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicScraperTest {



    @Test
    void testDynamicScraper() {
        DynamicScraper scraper = new DynamicScraper();

        scraper.createWebClient();

        String webPageURl = "https://news.ycombinator.com/login?goto=news";

        HtmlPage page = scraper.fillFormAndSubmit(webPageURl);
        assertEquals("draftdemoacct", scraper.getUserIdfromPage(page));

        HtmlPage page2 = scraper.getNextPage(page);
        assertEquals("https://news.ycombinator.com/news?p=2", scraper.getPageURL(page2));


        assertEquals("31.", scraper.getFirstItemIdFromPage(page2));

        scraper.printFirstEntryRow(page2);

        scraper.closeWebClient();
    }



    @Test
    void fillFormAndSubmit() {

    }

    @Test
    void getUserIdfromPage() {
    }

    @Test
    void getPageURL() {
    }

    @Test
    void getNextPage() {
    }

    @Test
    void getFirstItemIdFromPage() {
    }
}