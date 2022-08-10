package com.challenge.crawler.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Set<String> executing;
    private Set<String> success;
    private Set<String> skipped;
    private Set<String> errors;
}
