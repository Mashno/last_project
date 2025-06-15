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
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class MainGUI extends JFrame {
    private static final String APP_TITLE = "Анализатор текста";
    
    // Модель данных для таблицы
    private DefaultTableModel tableModel;
    private JTable frequencyTable;

    // Логика работы с файлами
    private FileOutputInput fileIO;
    private TextProcessor textProcessor;
    private FrequencyAnalyzer frequencyAnalyzer;
    private StopWordsLoader stopWordsLoader;
    private ExcelExporter excelExporter;

    // Данные
    private List<String> originalText;       // исходный текст
    private List<String> processedText;      // обработанный текст
    private List<String> stopWords;          // стоп-слова
    private AdvancedSettingsDialog settingsDialog;

    public MainGUI() {
        setTitle(APP_TITLE);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fileIO = new FileOutputInput(this);
        textProcessor = new TextProcessor();
        frequencyAnalyzer = new FrequencyAnalyzer();
        stopWordsLoader = new StopWordsLoader(fileIO);
        excelExporter = new ExcelExporter(this);
        //settingsDialog = new AdvancedSettingsDialog(this);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        // --- Меню ---
        JMenuBar menuBar = new JMenuBar();

        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem loadItem = new JMenuItem("Загрузить файл");
        JMenuItem analyzeItem = new JMenuItem("Провести анализ");
        JMenuItem saveReportItem = new JMenuItem("Сохранить отчёт");
        JMenuItem exitItem = new JMenuItem("Выход");

        loadItem.addActionListener(e -> onLoadFile());
        //analyzeItem.addActionListener(e -> onAnalyze());
        saveReportItem.addActionListener(e -> onSaveReport());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(loadItem);
        fileMenu.add(analyzeItem);
        fileMenu.add(saveReportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Меню "Настройки"
        JMenu settingsMenu = new JMenu("Настройки");
        JMenuItem loadStopWordsItem = new JMenuItem("Загрузить стоп-слова");
        JMenuItem advancedSettingsItem = new JMenuItem("Расширенные настройки");

        loadStopWordsItem.addActionListener(e -> onLoadStopWords());
        //advancedSettingsItem.addActionListener(e -> onOpenAdvancedSettings());

        settingsMenu.add(loadStopWordsItem);
        settingsMenu.add(advancedSettingsItem);

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);

        // --- Таблица ---
        String[] columnNames = {"Слово", "Частота (%)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        frequencyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(frequencyTable);

        // --- Статусная строка ---
        JLabel statusLabel = new JLabel("Готов к работе...");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        // --- Финальная сборка ---
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void onLoadFile() {
        originalText = fileIO.loadTextFile();
        if (originalText != null && !originalText.isEmpty()) {
            fileIO.showMessage("Загружено " + originalText.size() + " слов.");
        }
    }

//    private void onAnalyze() {
//        if (originalText == null || originalText.isEmpty()) {
//            fileIO.showError("Ошибка", new Exception("Сначала загрузите файл."));
//            return;
//        }
//
//        // Применяем настройки фильтрации
//        processedText = textProcessor.process(originalText, stopWords, settingsDialog.getSettings());
//
//        // Подсчёт частотности
//        Map<String, Double> frequencies = frequencyAnalyzer.calculateFrequencies(processedText);
//
//        // Очистка таблицы
//        tableModel.setRowCount(0);
//
//        // Заполнение таблицы
//        for (Map.Entry<String, Double> entry : frequencies.entrySet()) {
//            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
//        }
//
//        int deletedWords = originalText.size() - processedText.size();
//        fileIO.showMessage("Анализ завершён.\n" +
//                "Всего слов: " + originalText.size() + "\n" +
//                "Удалено слов: " + deletedWords + "\n" +
//                "Самое частое слово: " + getMostFrequentWord());
//    }
//
//    private String getMostFrequentWord() {
//        if (tableModel.getRowCount() > 0) {
//            return (String) tableModel.getValueAt(0, 0);
//        }
//        return "Нет данных";
//    }

    private void onSaveReport() {
        if (processedText == null || processedText.isEmpty()) {
            fileIO.showError("Ошибка", new Exception("Нет данных для сохранения."));
            return;
        }

        // Формируем отчёт как строку
        StringBuilder report = new StringBuilder();
        report.append("Отчёт о частоте слов:\n\n");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            report.append(tableModel.getValueAt(i, 0)).append(" — ").append(tableModel.getValueAt(i, 1)).append("\n");
        }

        // Сохраняем как Excel
        excelExporter.exportToExcel(tableModel, "output/report.xlsx");
    }

    private void onLoadStopWords() {
        stopWords = stopWordsLoader.loadStopWords();
        if (stopWords != null && !stopWords.isEmpty()) {
            fileIO.showMessage("Загружено " + stopWords.size() + " стоп-слов.");
        }
    }

//    private void onOpenAdvancedSettings() {
//        settingsDialog.setVisible(true);
//    }

   
}