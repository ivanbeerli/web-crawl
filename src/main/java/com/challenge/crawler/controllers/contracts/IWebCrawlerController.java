package com.challenge.crawler.controllers.contracts;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@RequestMapping(path = "/pages", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IWebCrawlerController {

    @GetMapping(path = "/search/{address}")
    ResponseEntity<Object> getResults(@Valid @NotEmpty @NotBlank @Length(min = 3) @PathVariable("address") String address);
}
