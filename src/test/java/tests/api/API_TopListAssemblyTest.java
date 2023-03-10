package tests.api;

import base.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class API_TopListAssemblyTest extends BaseTest {

    @Test
    public void test_API_AllTopListAssemblyLanguageLinksAreNotBroken() {
        String linkURL = "";
        int responseCode;
        int actualWorkingLinksCount = 0;

        List<WebElement> aTag =
                openBaseURL()
                        .clickTopListsMenu()
                        .clickTopRatedAssemblySubmenu()
                        .getLanguagesLinks();

        final int expectedWorkingLinksCount = aTag.size();
        int internalLinks = expectedWorkingLinksCount;

        for (WebElement link : aTag) {
            linkURL = link.getAttribute("href");

            if (linkURL != null && !linkURL.isBlank() && !linkURL.isEmpty()) {
                if (!linkURL.startsWith(getBaseUrl())) {
                    Reporter.log(linkURL + " is externalLink ", true);
                    internalLinks--;
                } else {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) (new URL(linkURL).openConnection());
                        connection.setRequestMethod("HEAD");
                        connection.connect();

                        responseCode = connection.getResponseCode();

                        if (responseCode < 400) {
                            actualWorkingLinksCount++;
                        } else {
                            Reporter.log(linkURL + " is broken, responseCode " + responseCode, true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Assert.assertEquals(actualWorkingLinksCount, internalLinks);
        Assert.assertEquals(actualWorkingLinksCount, expectedWorkingLinksCount);
    }
}
