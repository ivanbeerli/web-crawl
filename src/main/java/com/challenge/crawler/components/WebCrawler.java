package com.challenge.crawler.components;

import com.challenge.crawler.dtos.Page;
import com.challenge.crawler.dtos.PageResult;
import com.challenge.crawler.dtos.WebCrawlerResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WebCrawler {

    private static final Logger LOGGER = LogManager.getLogger(WebCrawler.class);

    private final Set<String> executing;
    private final Set<String> success;
    private final Set<String> skipped;
    private final Set<String> errors;
    private final List<Page> pages;

    @Autowired
    public WebCrawler(ResourceComponent resourceComponent) {
        this.pages = resourceComponent.getPages();
        executing = new HashSet<>();
        success = new HashSet<>();
        skipped = new HashSet<>();
        errors = new HashSet<>();
    }

    public WebCrawlerResult executeSearch(String address) {

        var pageResult = visit(address);
        boolean continueProcessing = processPageResults(pageResult);
        if (continueProcessing) {
            searchLinks(pageResult);
        }
        printReport();

        return WebCrawlerResult.builder()
                .success(success)
                .errors(errors)
                .skipped(skipped)
                .build();

    }

    private PageResult visit(String url) {
        return PageResult.builder()
                .url(url)
                .page(pages.stream()
                        .filter(page -> page.getAddress().equalsIgnoreCase(url))
                        .findFirst())
                .build();
    }


    private synchronized boolean processPageResults(PageResult pageResult) {
        executing.remove(pageResult.getUrl());
        if (pageResult.getPage().isEmpty()) {
            errors.add(pageResult.getUrl());
            return false;
        } else {
            success.add(pageResult.getUrl());
            return true;
        }
    }

    private void searchLinks(PageResult pageResult) {
        if (pageResult.getPage().isPresent()) {
            pageResult.getPage().get().getLinks()
                    .parallelStream()
                    .filter(this::shouldVisit)
                    .map(this::visit)
                    .forEach(result -> {
                        boolean next = processPageResults(result);
                        if (next) {
                            searchLinks(result);
                        }
                    });
        }
    }

    private synchronized boolean shouldVisit(String url) {
        if (success.contains(url)) {
            skipped.add(url);
            return false;
        }
        if (skipped.contains(url)) {
            return false;
        }
        if (errors.contains(url)) {
            skipped.add(url);
            return false;
        }
        if (executing.contains(url)) {
            skipped.add(url);
            return false;
        }
        executing.add(url);
        return true;
    }

    private void printReport() {
        LOGGER.info("Success: {}", success);
        LOGGER.info("Skipped: {}", skipped);
        LOGGER.info("Errors: {}", errors);
    }
}
