package org.nurgisa.tinyurl.services;

import org.nurgisa.tinyurl.models.Url;
import org.nurgisa.tinyurl.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.InvalidUrlException;

import java.io.IOException;
import java.net.*;
import java.util.Optional;
import java.util.UUID;

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
        String validatedUrl = validateAndNormalizeUrl(originalUrl);

        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(validatedUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        }

        String shortUrl;
        do {
            shortUrl = ShortUrlGenerator.generate(validatedUrl);
        }
        while (urlRepository.findByShortUrl(shortUrl).isPresent());

        Url url = new Url(validatedUrl, shortUrl);
        save(url);

        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);

        return url.map(Url::getOriginalUrl).orElse(null);
    }

    @Transactional
    public void save(Url url) {
        urlRepository.save(url);
    }

    private String validateAndNormalizeUrl(String url) {
        if (!url.matches("^(https?://).*")) {
            url = "https://" + url;
        }

        try {
            URI uri = new URI(url);
            return uri.toString(); // Lowercase and etc
        }
        catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
    }

    public boolean isValidUrl(String url) {
        url = validateAndNormalizeUrl(url);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            return (responseCode >= 200 && responseCode < 400); // 2xx and 3xx are valid responses
        }
        catch (IOException e) {
            return false;
        }
    }
}
