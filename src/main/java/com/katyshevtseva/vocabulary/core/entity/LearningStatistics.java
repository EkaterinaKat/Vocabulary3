package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "learning_statistics")
public class LearningStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int level;

    @Column(name = "all_num")
    private int allNum;

    @Column(name = "false_num")
    private int falseNum;

    @Temporal(TemporalType.DATE)
    private Date date;
}
