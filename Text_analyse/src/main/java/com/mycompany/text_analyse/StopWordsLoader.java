/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.text_analyse;

/**
 *
 * @author Владислав
 */
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StopWordsLoader {
    private final FileOutputInput fileIO;

    public StopWordsLoader(FileOutputInput fileHandler) {
        this.fileIO = fileHandler;
    }

    public List<String> loadStopWords() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (.txt)", "txt"));

        if (fileChooser.showOpenDialog(fileIO.getParentFrame()) == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            return readStopWordsFromFile(filePath);
        }
        return new ArrayList<>();
    }

    private List<String> readStopWordsFromFile(Path filePath) {
        try {
            Charset encoding = Charset.forName("Cp1251");
            List<String> lines = Files.readAllLines(filePath, encoding);//читает подряд все символы

            if (lines.isEmpty()) {
                fileIO.showError("Файл стоп-слов пуст.", new Exception());
                return new ArrayList<>();
            }

            String firstLine = lines.get(0).trim();//первая строка должна быть RU

            if (!firstLine.equalsIgnoreCase("RU")) {
                fileIO.showError("Первая строка файла должна содержать 'RU'", new Exception());
                return new ArrayList<>();
            }

            List<String> stopWords = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    stopWords.add(line.toLowerCase());
                }
            }

            fileIO.showMessage("Загружено " + stopWords.size() + " стоп-слов.");
            return stopWords;

        } catch (IOException ex) {
            fileIO.showError("Ошибка при чтении файла стоп-слов: " + filePath, ex);
            return new ArrayList<>();
        }
    }
}
