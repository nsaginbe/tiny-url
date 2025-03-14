package org.nurgisa.tinyurl.services;

import jakarta.servlet.http.HttpServletRequest;
import org.nurgisa.tinyurl.models.ClickEvent;
import org.nurgisa.tinyurl.models.Url;
import org.nurgisa.tinyurl.repositories.ClickEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClickEventService {

    private final ClickEventRepository clickEventRepository;

    @Autowired
    public ClickEventService(ClickEventRepository clickEventRepository) {
        this.clickEventRepository = clickEventRepository;
    }

    @Transactional
    public void logClickEvent(Url url, HttpServletRequest request) {
        ClickEvent clickEvent = new ClickEvent(
                url,
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );

        clickEventRepository.save(clickEvent);
    }
}
