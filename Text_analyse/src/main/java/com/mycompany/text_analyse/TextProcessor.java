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

    /**
     * Убирает стоп-слова из списка (если включено)
     * @param words список обработанных слов
     * @param stopWords список стоп-слов
     * @param removeStopWords флаг: убирать ли стоп-слова
     * @return отфильтрованный список слов
     */
    public List<String> removeStopWords(List<String> words, List<String> stopWords, boolean removeStopWords) {
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

    /**
     * Подсчитывает частоту встречаемости слов
     * @param words список слов после фильтрации
     * @return Map<Слово, Частота (%)>, отсортированный по убыванию
     */
    public Map<String, Double> calculateFrequencies(List<String> words) {
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

    /**
     * Округление до двух знаков после запятой
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
