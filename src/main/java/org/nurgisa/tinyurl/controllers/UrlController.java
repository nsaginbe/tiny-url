package org.nurgisa.tinyurl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.nurgisa.tinyurl.dto.UrlDTO;
import org.nurgisa.tinyurl.exceptions.ExistingShortUrlException;
import org.nurgisa.tinyurl.exceptions.InvalidUrlException;
import org.nurgisa.tinyurl.models.Url;
import org.nurgisa.tinyurl.services.ClickEventService;
import org.nurgisa.tinyurl.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;
    private final ClickEventService clickEventService;

    @Autowired
    public UrlController(UrlService urlService, ClickEventService clickEventService) {
        this.urlService = urlService;
        this.clickEventService = clickEventService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlDTO> shorten(@RequestParam(value = "url") String originalUrl,
                                          @RequestParam(value = "custom_short_url", required = false) String customShortUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new InvalidUrlException("URL cannot be empty");
        }
        if (!urlService.isReachableUrl(originalUrl)) {
            throw new InvalidUrlException("Invalid or unreachable URL");
        }

        String shortUrl;
        if (customShortUrl != null && !customShortUrl.isEmpty()) {
            try {
                shortUrl = urlService.checkForFree(originalUrl, customShortUrl);
            }
            catch (ExistingShortUrlException e) {
                throw new ExistingShortUrlException(e.getMessage());
            }
        }
        else {
            shortUrl = urlService.shortenUrl(originalUrl);
        }

        UrlDTO urlDTO = new UrlDTO(originalUrl, shortUrl);
        return ResponseEntity.ok(urlDTO);
    }

    @GetMapping("/r/{short_url}")
    public ResponseEntity<String> redirect(@PathVariable("short_url") String shortUrl,
                                           HttpServletRequest request) {
        Url url =  urlService.getUrl(shortUrl);

        if (url == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Short URL not found");
        }

        urlService.incrementClicks(url);
        clickEventService.logClickEvent(url, request);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url.getOriginalUrl())
                .build(); // Redirecting
    }

    @ExceptionHandler(InvalidUrlException.class)
    private ResponseEntity<String> handleException(InvalidUrlException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage()); // 400
    }

    @ExceptionHandler(ExistingShortUrlException.class)
    private ResponseEntity<String> handleException(ExistingShortUrlException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage()); // 400
    }
}
