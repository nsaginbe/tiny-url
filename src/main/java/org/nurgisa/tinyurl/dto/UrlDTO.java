package org.nurgisa.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UrlDTO {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt;

    public UrlDTO(String originalUrl, String shortUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.createdAt = LocalDateTime.now();
    }
}
