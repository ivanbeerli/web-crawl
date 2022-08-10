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

    private IWebCrawlerService webCrawler;
    private List<String> successPage01 = List.of("page-99", "page-01", "page-04", "page-05", "page-02", "page-03", "page-08", "page-09", "page-06", "page-07");
    private List<String> skippedPage01 = List.of("page-01", "page-10", "page-04", "page-05", "page-02", "page-03", "page-08", "page-09");
    private List<String> errorPage01 = List.of("page-00", "page-11", "page-12", "page-10", "page-13");
    private List<String> successPage50 = List.of("page-51", "page-52", "page-50");
    private List<String> skippedPage50 = List.of("page-50");
    private List<String> errorPage50 = List.of("page-53");
    @Autowired
    public WebCrawlerServiceTest(IWebCrawlerService webCrawler){
        this.webCrawler = webCrawler;
    }

    @Test
    public void shouldSuccessGetInformation(){
        Assertions.assertDoesNotThrow(() -> webCrawler.crawlPage("page-01"));
    }

    @Test
    public void shouldSuccessGetInformation_checkData(){

        var result = webCrawler.crawlPage("page-01");

        assertResult01(result);


        var result2 = webCrawler.crawlPage("page-50");

        assertResult50(result2);

        List.of("page-01","page-50").parallelStream().forEach(url -> {
                    var results = webCrawler.crawlPage(url);
                    if(url.equalsIgnoreCase("page-01")){
                        assertResult01(results);
                    }else if(url.equalsIgnoreCase("page-50")){
                        assertResult50(results);
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

    private void assertResult50(WebCrawlerResult result2) {
        Assertions.assertEquals(result2.getSuccess().size(), successPage50.size());
        Assertions.assertTrue(result2.getSuccess().containsAll(successPage50));
        Assertions.assertTrue(successPage50.containsAll(result2.getSuccess()));

        Assertions.assertEquals(result2.getSkipped().size(), skippedPage50.size());
        Assertions.assertTrue(result2.getSkipped().containsAll(skippedPage50));
        Assertions.assertTrue(skippedPage50.containsAll(result2.getSkipped()));

        Assertions.assertEquals(result2.getErrors().size(), errorPage50.size());
        Assertions.assertTrue(result2.getErrors().containsAll(errorPage50));
        Assertions.assertTrue(errorPage50.containsAll(result2.getErrors()));
    }

    @Test
    public void shouldFailGetInformation_invalidAddress(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> webCrawler.crawlPage("pa") );
    }
}
