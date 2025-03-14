package org.nurgisa.tinyurl.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "click_event")
@Getter
@Setter
@NoArgsConstructor
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "url_id", referencedColumnName = "id", nullable = false)
    private Url url;

    @Column(name = "timestamp", nullable = false, updatable = false, insertable = false)
    private LocalDateTime timestamp;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    public ClickEvent(Url url, String ipAddress, String userAgent) {
        this.url = url;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
}
