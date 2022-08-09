package com.challenge.crawler.services.implementations;

import com.challenge.crawler.components.ResourceComponent;
import com.challenge.crawler.dtos.Page;
import com.challenge.crawler.dtos.WebCrawlerResult;
import com.challenge.crawler.services.contracts.IWebCrawlerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
@Validated
@Scope("prototype")
public class WebCrawlerService implements IWebCrawlerService {


    private static final Logger LOGGER = LogManager.getLogger(WebCrawlerService.class);

    private Set<String> success;
    private Set<String> skipped;
    private Set<String> errors;
    List<Page> pages;
    ResourceComponent resourceComponent;

    @Autowired
    public WebCrawlerService(ResourceComponent resourceComponent){
        this.resourceComponent = resourceComponent;
        success = new HashSet<>();
        skipped = new HashSet<>();
        errors = new HashSet<>();
        pages = resourceComponent.getPages();
    }

    public synchronized WebCrawlerResult crawlPage(String url){
        success = new HashSet<>();
        skipped = new HashSet<>();
        errors = new HashSet<>();
        crawl(url);
        printReport();
        return WebCrawlerResult.builder()
                .success(success)
                .skipped(skipped)
                .errors(errors)
                .build();
    }

    private void crawl(String url) {
        if(success.contains(url) ){
            skipped.add(url);
            return;
        }
        if(errors.contains(url) ){
            skipped.add(url);
            return;
        }

        Optional<Page> optionalPage = visit(url);

        if(optionalPage.isPresent()){
            success.add(url);
            optionalPage.get().getLinks()
                    .parallelStream()
                    .forEach(page -> crawl(page));
        }else{
            errors.add(url);
        }
    }

    private Optional<Page> visit(String url){
        return pages.stream()
                .filter( page -> page.getAddress().equalsIgnoreCase(url))
                .findFirst();
    }

    private void printReport(){
        LOGGER.info("Success: {}", success);
        LOGGER.info("Skipped: {}", skipped);
        LOGGER.info("Errors: {}", errors);
    }
}
