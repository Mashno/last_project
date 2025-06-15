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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileOutputInput {
    private final JFrame parentFrame;

    public FileOutputInput(JFrame parent) {
        this.parentFrame = parent;
    }

    public List<String> loadTextFile() {//загрузка файла
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (.txt)", "txt"));

        if (fileChooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            return readWordsFromFile(filePath);
        }
        return new ArrayList<>();
    }

    
    private List<String> readWordsFromFile(Path filePath) {//обработка слов
        try {
            Charset encoding = Charset.forName("Cp1251");

            String content = Files.readString(filePath, encoding);//читает файл как одну строку
            String[] words = content.split("\\s+");//разделяет текст на слова поделив по одному или нескольким пробелам
            List<String> wordList = new ArrayList<>();

            for (String word : words) {
                String cleaned = word.trim();//trim - убирает пробелы в начале и конце. replaceAll - удаляет все символы кроме русских
                if (!cleaned.isEmpty()) {
                    wordList.add(cleaned.toLowerCase());
                }
            }

            showMessage("Файл успешно загружен: " + filePath.getFileName());
            return wordList;

        } catch (IOException ex) {
            showError("Ошибка при чтении файла: " + filePath, ex);
            return new ArrayList<>();
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(
                parentFrame,
                message + "\n" + ex.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
        ex.printStackTrace();
    }
    
    
    public JFrame getParentFrame() {
        return parentFrame;
    }
}