package com.challenge.crawler.services.contracts;

import com.challenge.crawler.dtos.WebCrawlerResult;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
public interface IWebCrawlerService {
    WebCrawlerResult crawlPage(@Valid @NotEmpty @NotBlank @Length(min = 3) String url);
}
