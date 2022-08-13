package com.challenge.crawler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebCrawlerResult {

    private List<String> success;
    private Set<String> skipped;
    private List<String> errors;
}
