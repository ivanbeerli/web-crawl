package com.challenge.crawler.services.implementations;

import com.challenge.crawler.components.ResourceComponent;
import com.challenge.crawler.dtos.Page;
import com.challenge.crawler.dtos.PageResult;
import com.challenge.crawler.dtos.WebCrawlerResult;
import com.challenge.crawler.services.contracts.IWebCrawlerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;

@Component
@Validated
public class WebCrawlerService implements IWebCrawlerService {


    private static final Logger LOGGER = LogManager.getLogger(WebCrawlerService.class);

    private List<Page> pages;
    private ResourceComponent resourceComponent;

    @Autowired
    public WebCrawlerService(ResourceComponent resourceComponent){
        this.resourceComponent = resourceComponent;
        pages = resourceComponent.getPages();
    }

    public WebCrawlerResult crawlPage(String url){
        var webSearch=  WebCrawlerResult.builder()
                .success(new HashSet<>())
                .skipped(new HashSet<>())
                .errors(new HashSet<>())
                .executing(new HashSet<>())
                .build();
        var pageResult = visit(url);
        boolean continueProcessing = processPageResults(pageResult, webSearch);
        if(continueProcessing){
            searchLinks(pageResult, webSearch);
        }
        printReport(webSearch);
        return webSearch;
    }

    private PageResult visit(String url){
        return PageResult.builder()
                .url(url)
                .page(pages.stream()
                        .filter( page -> page.getAddress().equalsIgnoreCase(url))
                        .findFirst())
                .build();
    }


    private boolean processPageResults(PageResult pageResult, WebCrawlerResult webSearch){
        synchronized (webSearch) {
            webSearch.getExecuting().remove(pageResult.getUrl());
            if (pageResult.getPage().isEmpty()) {
                webSearch.getErrors().add(pageResult.getUrl());
                return false;
            }else{
                webSearch.getSuccess().add(pageResult.getUrl());
                return true;
            }
        }
    }

    private void searchLinks(PageResult pageResult, WebCrawlerResult webSearch){
        pageResult.getPage().get().getLinks()
                .parallelStream()
                    .filter( link -> shouldVisit(link, webSearch))
                    .map( link -> visit(link))
                    .forEach( result -> {   boolean next = processPageResults(result, webSearch);
                            if(next){
                                searchLinks(result, webSearch);
                            }
                        });
    }

    private boolean shouldVisit(String url, WebCrawlerResult webSearch){
        synchronized (webSearch) {
            if (webSearch.getSuccess().contains(url)) {
                webSearch.getSkipped().add(url);
                return false;
            }
            if (webSearch.getSkipped().contains(url)) {
                return false;
            }
            if (webSearch.getErrors().contains(url)) {
                webSearch.getSkipped().add(url);
                return false;
            }
            if (webSearch.getExecuting().contains(url)) {
                webSearch.getSkipped().add(url);
                return false;
            }
            webSearch.getExecuting().add(url);
            return true;
        }
    }

    private void printReport(WebCrawlerResult webSearch){
        LOGGER.info("Success: {}", webSearch.getSuccess());
        LOGGER.info("Skipped: {}", webSearch.getSkipped());
        LOGGER.info("Errors: {}", webSearch.getErrors());
    }


}
