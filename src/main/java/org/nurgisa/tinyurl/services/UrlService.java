package org.nurgisa.tinyurl.services;

import org.nurgisa.tinyurl.exceptions.ExistingShortUrlException;
import org.nurgisa.tinyurl.models.Url;
import org.nurgisa.tinyurl.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.*;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UrlService {
    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public String shortenUrl(String originalUrl) {
        String validUrl = validateUrl(originalUrl);

        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(validUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        }

        String shortUrl;
        do {
            shortUrl = ShortUrlGenerator.generate(validUrl);
        }
        while (urlRepository.findByShortUrl(shortUrl).isPresent());

        Url url = new Url(validUrl, shortUrl);
        urlRepository.save(url);

        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);

        return url.map(Url::getOriginalUrl).orElse(null);
    }

    public Url getUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl).orElse(null);
    }

    @Transactional
    public void incrementClicks(Url url) {
        url.setClicks(url.getClicks() + 1);
        urlRepository.save(url);
    }

    @Transactional
    public void save(Url url) {
        urlRepository.save(url);
    }

    private String validateUrl(String url) {
        if (!url.matches("^(https?://).*")) {
            url = "https://" + url;
        }

        try {
            URL validUrl = new URL(url);
            return validUrl.toString(); // Lowercase and etc
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
    }

    public boolean isReachableUrl(String url) {
        try {
            String validUrl = validateUrl(url);

            HttpURLConnection connection = (HttpURLConnection) new URL(validUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            return (responseCode >= 200 && responseCode < 400); // 2xx and 3xx are valid responses
        }
        catch (MalformedURLException e) {
            return false; // Invalid URL
        }
        catch (IOException e) {
            return false; // Unreachable URL
        }
    }

    @Transactional
    public String checkForFree(String originalUrl, String customShortUrl) {
        String validUrl = validateUrl(originalUrl);

        if (urlRepository.findByShortUrl(customShortUrl).isPresent()) {
            throw new ExistingShortUrlException("Short url already exists");
        }

        Url url = new Url(validUrl, customShortUrl);
        urlRepository.save(url);

        return customShortUrl;
    }
}
