package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "frequent_word")
public class FrequentWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String translation;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "frequentWord")
    private List<Entry> entries = new ArrayList<>();

    public enum Status {
        INTACT, NEED_TO_LEARN, LEARNED, KNOWN
    }

    public String getWordAndTranslation() {
        return word + " - " + translation;
    }
}
