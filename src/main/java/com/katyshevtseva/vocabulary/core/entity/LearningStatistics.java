package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LearningStatistics {
    private int level;
    private int allNum;
    private int falseNum;
    private Date date;
}
