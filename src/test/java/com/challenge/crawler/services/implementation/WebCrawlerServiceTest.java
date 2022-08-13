package com.challenge.crawler.services.implementation;

import com.challenge.crawler.dtos.WebCrawlerResult;
import com.challenge.crawler.services.contracts.IWebCrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.List;

@SpringBootTest
public class WebCrawlerServiceTest {

    private static final String PAGE_01 = "page-01";
    private static final String PAGE_50 = "page-50";
    private static final String PAGE_60 = "page-60";

    private IWebCrawlerService webCrawler;

    private final List<String> successPage01;
    private final List<String> skippedPage01;
    private final List<String> errorPage01;

    private final List<String> successPage50;
    private final List<String> skippedPage50;
    private final List<String> errorPage50;

    private final List<String> errorPage60;


    @Autowired
    public WebCrawlerServiceTest(IWebCrawlerService webCrawler){
        this.webCrawler = webCrawler;

        successPage01 = List.of("page-99", "page-01", "page-04", "page-05", "page-02", "page-03", "page-08", "page-09", "page-06", "page-07");
        skippedPage01 = List.of("page-01", "page-10", "page-04", "page-05", "page-02", "page-03", "page-08", "page-09");
        errorPage01 = List.of("page-00", "page-11", "page-12", "page-10", "page-13");

        successPage50 = List.of("page-51", "page-52", "page-50");
        skippedPage50 = List.of("page-50");
        errorPage50 = List.of("page-53");

        errorPage60 = List.of("page-60");

    }

    @Test
    public void shouldSuccessGetInformation(){
        Assertions.assertDoesNotThrow(() -> webCrawler.crawlPage("page-01"));
    }

    @Test
    public void shouldSuccessGetInformation_checkData(){

        var result = webCrawler.crawlPage(PAGE_01);

        assertResult01(result);

        var result2 = webCrawler.crawlPage(PAGE_50);

        assertResult50(result2);

        var result3 = webCrawler.crawlPage(PAGE_60);

        assertResult60(result3);

        List.of(PAGE_01, PAGE_50, PAGE_60).parallelStream()
                .forEach(url -> {
                    var results = webCrawler.crawlPage(url);
                    switch (url){
                        case PAGE_01:
                            assertResult01(results);
                            break;
                        case PAGE_50:
                            assertResult50(results);
                            break;
                        case PAGE_60:
                            assertResult60(results);
                            break;
                    }
                });
    }

    private void assertResult01(WebCrawlerResult result) {
        Assertions.assertEquals(result.getSuccess().size(), successPage01.size());
        Assertions.assertTrue(result.getSuccess().containsAll(successPage01));
        Assertions.assertTrue(successPage01.containsAll(result.getSuccess()));

        Assertions.assertEquals(result.getSkipped().size(), skippedPage01.size());
        Assertions.assertTrue(result.getSkipped().containsAll(skippedPage01));
        Assertions.assertTrue(skippedPage01.containsAll(result.getSkipped()));

        Assertions.assertEquals(result.getErrors().size(), errorPage01.size());
        Assertions.assertTrue(result.getErrors().containsAll(errorPage01));
        Assertions.assertTrue(errorPage01.containsAll(result.getErrors()));
    }

    private void assertResult50(WebCrawlerResult result) {
        Assertions.assertEquals(result.getSuccess().size(), successPage50.size());
        Assertions.assertTrue(result.getSuccess().containsAll(successPage50));
        Assertions.assertTrue(successPage50.containsAll(result.getSuccess()));

        Assertions.assertEquals(result.getSkipped().size(), skippedPage50.size());
        Assertions.assertTrue(result.getSkipped().containsAll(skippedPage50));
        Assertions.assertTrue(skippedPage50.containsAll(result.getSkipped()));

        Assertions.assertEquals(result.getErrors().size(), errorPage50.size());
        Assertions.assertTrue(result.getErrors().containsAll(errorPage50));
        Assertions.assertTrue(errorPage50.containsAll(result.getErrors()));
    }

    private void assertResult60(WebCrawlerResult result) {
        Assertions.assertEquals(result.getSuccess().size(), 0);

        Assertions.assertEquals(result.getSkipped().size(), 0);

        Assertions.assertEquals(result.getErrors().size(), errorPage60.size());
        Assertions.assertTrue(result.getErrors().containsAll(errorPage60));
        Assertions.assertTrue(errorPage60.containsAll(result.getErrors()));
    }


    @Test
    public void shouldFailGetInformation_invalidAddress(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> webCrawler.crawlPage("pa") );
    }
}
