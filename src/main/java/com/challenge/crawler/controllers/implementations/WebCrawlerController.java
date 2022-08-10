package com.challenge.crawler.controllers.implementations;

import com.challenge.crawler.controllers.contracts.IWebCrawlerController;
import com.challenge.crawler.dtos.WebCrawlerResult;
import com.challenge.crawler.services.contracts.IWebCrawlerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class WebCrawlerController implements IWebCrawlerController {

    private static final Logger LOGGER = LogManager.getLogger(WebCrawlerController.class);

    private IWebCrawlerService webCrawler;

    @Autowired
    public WebCrawlerController(IWebCrawlerService webCrawler){
        this.webCrawler = webCrawler;
    }

    @Override
    public ResponseEntity<Object> getResults(String address){
        try {
            LOGGER.info("Start web searching for address [{}]", address);
            WebCrawlerResult result = webCrawler.crawlPage(address);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error("Error on web searching for address [{}]", address);
            return  new ResponseEntity<>("Error on web searching",HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            LOGGER.info("Finish web searching for address [{}]", address);

        }
    }
}
