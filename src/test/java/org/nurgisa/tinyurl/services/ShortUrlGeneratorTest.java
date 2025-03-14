package org.nurgisa.tinyurl.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShortUrlGeneratorTest {

    @Test
    void generate_WhenTheSameUrl_ThenTheSameShortUrl() {
        String url1 = "https://www.google.com";
        String url2 = "https://www.google.com";

        String shortUrl1 = ShortUrlGenerator.generate(url1);
        String shortUrl2 = ShortUrlGenerator.generate(url2);

        assertEquals(shortUrl1, shortUrl2);
    }
}
