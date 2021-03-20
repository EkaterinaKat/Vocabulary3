package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String translation;

    private int level;

    @ManyToOne
    @JoinColumn(name = "word_list_id")
    private WordList wordList;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_repeat")
    private Date lastRepeat;

    @Override
    public String toString() {
        return "Entry{" +
                "word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
