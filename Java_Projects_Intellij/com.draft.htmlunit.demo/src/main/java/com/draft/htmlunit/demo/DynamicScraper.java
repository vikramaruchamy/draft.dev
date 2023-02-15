package com.draft.htmlunit.demo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;

public class DynamicScraper {
    private WebClient webClient;

    public static void main(String[] args) {
        DynamicScraper scraper = new DynamicScraper();

        scraper.createWebClient();

        String webPageURl = "https://news.ycombinator.com/login?goto=news";

        HtmlPage page = scraper.fillFormAndSubmit(webPageURl);

        System.out.println(scraper.getUserIdfromPage(page));

        HtmlPage page2 = scraper.getNextPage(page);

        System.out.println(scraper.getPageURL(page2));

        System.out.println(scraper.getFirstItemIdFromPage(page2));

        scraper.printFirstEntryRow(page2);

        scraper.closeWebClient();
    }

    public void createWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
    }

    public void closeWebClient() {
        webClient.close();
    }

    public HtmlPage fillFormAndSubmit(String webPageURL) {

        HtmlPage page = null;
        try {
            // Get the first page

            HtmlPage signUpPage = webClient.getPage(webPageURL);

            // Get the form using its index. 0 returns the first form.
            HtmlForm form = signUpPage.getForms().get(0);

            //Get the Username and Password field using its name
            HtmlTextInput userField = form.getInputByName("acct");
            HtmlInput pwField = form.getInputByName("pw");

            //Set the User name and Password in the appropriate fields
            userField.setValueAttribute("draftdemoacct");
            pwField.setValueAttribute("test@12345");

            //Get the submit button uisng its Value
            HtmlSubmitInput submitButton = form.getInputByValue("login");

            //Click the submit button and it'll return the target page of the submit button
            page = submitButton.click();


        } catch (FailingHttpStatusCodeException | IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    public String getUserIdfromPage(HtmlPage page) {
        return page.getElementById("me").getTextContent();
    }

    public String getPageURL(HtmlPage page) {
        return page.getUrl().toString();
    }

    public HtmlPage getNextPage(HtmlPage page) {

        HtmlPage nextPage = null;

        try {
            List<HtmlAnchor> links = (List<HtmlAnchor>)(Object)page.getByXPath("html/body/center/table/tbody/tr[3]/td/table/tbody/tr[92]/td[2]/a");
            HtmlAnchor anchor =  links.get(0);
            nextPage = anchor.click();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nextPage;
    }

    public String getFirstItemIdFromPage(HtmlPage page) {

        String firstItemId = null;

        List<Object> entries = page.getByXPath("/html/body/center/table/tbody/tr[3]/td/table/tbody/tr[1]/td[1]/span");

        HtmlSpan span = (HtmlSpan) (entries.get(0));

        firstItemId = span.getTextContent();

        return firstItemId;
    }

    public void printFirstEntryRow(HtmlPage page) {

        List<Object> entries2 = page.getByXPath("/html/body/center/table/tbody/tr[3]/td/table/tbody/tr[1]");

        HtmlTableRow row = (HtmlTableRow) (entries2.get(0));

        for (final HtmlTableCell cell : row.getCells()) {

            System.out.println(cell.asNormalizedText() + ',');

        }
    }

}
