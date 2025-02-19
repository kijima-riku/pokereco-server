package com.pokereco.pokereco.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "decks")
public class Decks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , length = 128)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at" , updatable = false)
            private LocalDateTime createdAt;

    public Decks() {}

    public Decks (final Integer id , final String name, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Integer getId() {return id;}

    public String getName() {return name;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
