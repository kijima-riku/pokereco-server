package com.pokereco.pokereco.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , length = 128)
    private String mainName;

    @Column(nullable = true , length = 128)
    private String subName;

    @CreationTimestamp
    @Column(name = "created_at" , updatable = false)
            private LocalDateTime createdAt;

    public Deck() {}

    public Deck(final String mainName,final String subName) {
        this.mainName = mainName;
        this.subName = subName;
    }

    public Integer getId() {return id;}

    public String getMainName() {return mainName;}

    public String getSubName() {return subName;}
}
