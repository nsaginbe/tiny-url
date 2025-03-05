package org.nurgisa.tinyurl.repositories;

import org.nurgisa.tinyurl.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Integer> {
    Optional<Url> findByOriginalUrl(String validatedUrl);
    Optional<Url> findByShortUrl(String shortUrl);
}
