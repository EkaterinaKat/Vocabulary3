package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.FrequentWord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Specials {

    private void transferWordsFromFileToDb() {
        Date start = new Date();
        try {
            String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\814571\\Desktop\\words.txt")));
            List<String> lines = Arrays.asList(content.split("\n"));
            for (String s : lines) {
                s = s.replaceAll("<tr><td>[\\d]*</td><td>", "");
                s = s.replaceAll("</td><td>", "!");
                s = s.replaceAll("</td></tr>", "");
                String[] pair = s.split("!");

                FrequentWord frequentWord = new FrequentWord();
                frequentWord.setWord(pair[0].trim());
                frequentWord.setTranslation(pair[1].trim());
                frequentWord.setStatus(FrequentWord.Status.INTACT);
//                dao.saveNewFrequentWord(frequentWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date end = new Date();
        System.out.println("Время выполнения в миллисекундах: " + (end.getTime() - start.getTime()));
    }
}
