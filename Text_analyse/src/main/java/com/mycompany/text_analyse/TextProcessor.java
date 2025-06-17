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
            return words; //ничего не убираем
        }

        Set<String> stopSet = new HashSet<>(stopWords);//для лучшей скорости обработки и хранения уникальных значений
        return words.stream()
                .filter(word -> !stopSet.contains(word.toLowerCase()))//приводим к нижнему регистру, проверяем есть ли слово в списке стоп слов, если нет, то оставляем
                .collect(Collectors.toList());//остатки собираем в список
    }

    
    public Map<String, Double> calculateFrequencies(List<String> words) {//считает частоту, words-слова после фильтрации
        if (words == null || words.isEmpty()) {
            return Collections.emptyMap();
        }

        int totalWords = words.size();

        return words.stream()//создаём поток из слов
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))//создаёт Map в котором ключ- слово, значение - количество слов
                .entrySet().stream()//преобразование в поток ключ- значение
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())//сортировка по значению по убыванию
                .collect(Collectors.toMap(//создаём Map в котором слово-частота
                        Map.Entry::getKey,//берём ключ
                        entry -> roundToFourDecimals((double) entry.getValue() / totalWords * 100),//считаем частоту
                        (oldValue, newValue) -> oldValue,//если ключи повторяются, то остаётся только старое значение
                        LinkedHashMap::new//запоминается порядок добавления элементов, :: - создание новой ссылки на Map
                ));
    }

    
    private double roundToFourDecimals(double value) {//округление
        return Math.round(value * 10000.0) / 10000.0;
    }
}
