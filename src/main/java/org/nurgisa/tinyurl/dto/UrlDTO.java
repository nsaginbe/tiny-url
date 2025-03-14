package org.nurgisa.tinyurl.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Immutable
@Getter
public class UrlDTO {
    private final String originalUrl;
    private final String shortUrl;
    private final LocalDateTime createdAt;

    public UrlDTO(String originalUrl, String shortUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.createdAt = LocalDateTime.now();
    }
}
