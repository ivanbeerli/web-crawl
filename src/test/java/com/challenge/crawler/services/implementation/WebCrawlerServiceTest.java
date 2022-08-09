package com.challenge.crawler.services.implementation;

import com.challenge.crawler.dtos.WebCrawlerResult;
import com.challenge.crawler.services.contracts.IWebCrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

@SpringBootTest
public class WebCrawlerServiceTest {

    IWebCrawlerService webCrawler;

    @Autowired
    public WebCrawlerServiceTest(IWebCrawlerService webCrawler){
        this.webCrawler = webCrawler;
    }

    @Test
    public void shouldSuccessGetInformation(){
        Assertions.assertDoesNotThrow(() -> webCrawler.crawlPage("page-50"));
    }

    @Test
    public void shouldSuccessGetInformation_checkData(){
        WebCrawlerResult result = webCrawler.crawlPage("page-50");
        Assertions.assertEquals(result.getSuccess().contains("page-50") ,true);
    }

    @Test
    public void shouldFailGetInformation_invalidAddress(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> webCrawler.crawlPage("pa") );
    }
}
