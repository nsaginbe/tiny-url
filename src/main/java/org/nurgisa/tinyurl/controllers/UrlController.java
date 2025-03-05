package org.nurgisa.tinyurl.controllers;

import org.nurgisa.tinyurl.dto.UrlDTO;
import org.nurgisa.tinyurl.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlDTO> shorten(@RequestParam("url") String originalUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new InvalidUrlException("URL cannot be empty");
        }
        if (!urlService.isValidUrl(originalUrl)) {
            throw new InvalidUrlException("Invalid or unreachable URL");
        }

        String shortUrl = urlService.shortenUrl(originalUrl);

        UrlDTO urlDTO = new UrlDTO(originalUrl, shortUrl);
        return new ResponseEntity<>(urlDTO, HttpStatus.OK); // 200
    }

    @GetMapping("/r/{short_url}")
    public ResponseEntity<String> redirect(@PathVariable("short_url") String shortUrl) {
        String originalUrl = urlService.getOriginalUrl(shortUrl);

        if (originalUrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short URL not found");
        }

        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, originalUrl).build();
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(InvalidUrlException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage()); // 400
    }
}
