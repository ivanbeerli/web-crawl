package com.challenge.crawler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebCrawlerResult {
    private Set<String> success;
    private Set<String> skipped;
    private Set<String> errors;
}
