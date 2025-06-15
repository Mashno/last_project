/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.text_analyse;

/**
 *
 * @author Владислав
 */
import java.util.*;
import java.util.stream.Collectors;

public class TextProcessor {

    
    public List<String> removeStopWords(List<String> words, List<String> stopWords, boolean removeStopWords) {//удаление стоп слов
        if (words == null || words.isEmpty()) {
            return Collections.emptyList();
        }

        if (!removeStopWords || stopWords == null || stopWords.isEmpty()) {
            return words; // ничего не убираем
        }

        Set<String> stopSet = new HashSet<>(stopWords);
        return words.stream()
                .filter(word -> !stopSet.contains(word.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<String> filterByLanguage(List<String> words, String language) {
        if (words == null || words.isEmpty()) {
            return Collections.emptyList();
        }

        if ("RU".equals(language)) {
            return words.stream()
                    .map(word -> word.replaceAll("[^а-яА-Я]", "").trim())
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.toList());
        } else if ("EN".equals(language)) {
            return words.stream()
                    .map(word -> word.replaceAll("[^a-zA-Z]", "").trim())
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.toList());
        }

        return words; 
    }
    
    public Map<String, Double> calculateFrequencies(List<String> words) {//считает частоту, words-слова после фильтрации
        if (words == null || words.isEmpty()) {
            return Collections.emptyMap();
        }

        int totalWords = words.size();

        return words.stream()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> roundToTwoDecimals((double) entry.getValue() / totalWords * 100),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    
    private double roundToTwoDecimals(double value) {//округление
        return Math.round(value * 10000.0) / 10000.0;
    }
}
