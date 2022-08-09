package com.challenge.crawler.contracts.implementations;

import com.challenge.crawler.controllers.implementations.WebCrawlerController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;

@SpringBootTest
public class WebCrawlerControllerTest {

    @Autowired
    WebCrawlerController webCrawler;

    @Test
    public void shouldSuccessGetInformation(){
        Assertions.assertDoesNotThrow(() -> webCrawler.getResults("page-50"));
    }

    @Test
    public void shouldSuccessGetInformation_checkInfo(){
        ResponseEntity<Object> response = webCrawler.getResults("page-50");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldFailGetInformation_invalidAddress(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> webCrawler.getResults("pa") );
    }
}
