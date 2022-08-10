package com.challenge.crawler.services.implementations;

import com.challenge.crawler.components.WebCrawler;
import com.challenge.crawler.dtos.WebCrawlerResult;
import com.challenge.crawler.services.contracts.IWebCrawlerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class WebCrawlerService implements IWebCrawlerService {

    private static final Logger LOGGER = LogManager.getLogger(WebCrawlerService.class);

    public WebCrawlerResult crawlPage(String url){
        LOGGER.info("Starting url [{}]", url);
        return webCrawler().executeSearch(url);
    }

    @Lookup
    public WebCrawler webCrawler(){
        return null;
    }

}
