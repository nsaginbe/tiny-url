package org.nurgisa.tinyurl.repositories;

import org.nurgisa.tinyurl.models.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Integer> {
}
