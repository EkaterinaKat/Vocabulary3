package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "learning_log")
public class LearningLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @Column(name = "init_level")
    private int initLevel;

    @Column(name = "positive_answer")
    private boolean positiveAnswer;

    @Temporal(TemporalType.DATE)
    private Date date;
}
