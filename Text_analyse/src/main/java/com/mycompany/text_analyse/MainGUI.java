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

    
    private DefaultTableModel tableModel;
    private JTable frequencyTable;

    private FileOutputInput fileIO;
    private TextProcessor textProcessor;
    private StopWordsLoader stopWordsLoader;
    private ExcelExporter excelExporter;
    private AdvancedSettingsDialog settingsDialog;

    
    private List<String> originalText;//исходный текст
    private List<String> processedText;//обработанный текст
    private List<String> stopWords;//стоп-слова

    public MainGUI() {
        setTitle(APP_TITLE);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setVisible(true);

       
        fileIO = new FileOutputInput(this);
        textProcessor = new TextProcessor();
        stopWordsLoader = new StopWordsLoader(fileIO);
        excelExporter = new ExcelExporter(this);
        settingsDialog = new AdvancedSettingsDialog(this);

        initializeUI();
    }

    private void initializeUI() {
        
        JMenuBar menuBar = new JMenuBar();
         //меню файл
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem loadItem = new JMenuItem("Загрузить файл");
        JMenuItem analyzeItem = new JMenuItem("Провести анализ");
        JMenuItem saveReportItem = new JMenuItem("Сохранить отчёт");
        JMenuItem exitItem = new JMenuItem("Выход");

        loadItem.addActionListener(e -> onLoadFile());//лямбда выражения вызова методов обработки кнопок
        analyzeItem.addActionListener(e -> onAnalyze());
        saveReportItem.addActionListener(e -> onSaveReport());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(loadItem);
        fileMenu.add(analyzeItem);
        fileMenu.add(saveReportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        //меню настройки
        JMenu settingsMenu = new JMenu("Настройки");
        JMenuItem loadStopWordsItem = new JMenuItem("Загрузить стоп-слова");
        JMenuItem advancedSettingsItem = new JMenuItem("Расширенные настройки");

        loadStopWordsItem.addActionListener(e -> onLoadStopWords());
        advancedSettingsItem.addActionListener(e -> onOpenAdvancedSettings());

        settingsMenu.add(loadStopWordsItem);
        settingsMenu.add(advancedSettingsItem);

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);

        //таблица
        String[] columnNames = {"Слово", "Частота (%)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        frequencyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(frequencyTable);

        
        JLabel statusLabel = new JLabel("Готов к работе...");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

       
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        //кнопка провести анализ
        JButton analyzeButton = new JButton("Провести анализ");
        analyzeButton.addActionListener(e -> onAnalyze());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(analyzeButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onLoadFile() {//загрузка файла
        originalText = fileIO.loadTextFile();
        if (originalText != null && !originalText.isEmpty()) {
            fileIO.showMessage("Загружено " + originalText.size() + " слов.");
        }
    }

    private void onAnalyze() {//анализ текста
        if (originalText == null || originalText.isEmpty()) {
            fileIO.showError("Ошибка", new Exception("Сначала загрузите файл."));
            return;
        }

        //настройки фильтрации
        boolean shouldRemoveStopWords = settingsDialog.shouldRemoveStopWords();
        processedText = textProcessor.removeStopWords(originalText, stopWords, shouldRemoveStopWords);//обработка текста

        //подсчёт частоты
        Map<String, Double> frequencies = textProcessor.calculateFrequencies(processedText);//подсчёт частоты
        tableModel.setRowCount(0);

        //заполнение таблицы
        for (Map.Entry<String, Double> entry : frequencies.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        int deletedWords = originalText.size() - processedText.size();//количество удалённых слов
        fileIO.showMessage("Анализ завершён.\n" +
                "Всего слов: " + originalText.size() + "\n" +
                "Удалено слов: " + deletedWords + "\n" +
                "Самое частое слово: " + getMostFrequentWord());
    }

    private String getMostFrequentWord() {//выбор самого популярного слова в первой строке
        if (tableModel.getRowCount() > 0) {
            return (String) tableModel.getValueAt(0, 0);
        }
        return "Нет данных";
    }

    private void onSaveReport() {//сохранения анализа текста в excel
        if (processedText == null || processedText.isEmpty()) {
            fileIO.showError("Ошибка", new Exception("Нет данных для сохранения."));
            return;
        }

        StringBuilder report = new StringBuilder();//класс джавы для ускорения работы со строками
        report.append("Отчёт о частоте слов:\n\n");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            report.append(tableModel.getValueAt(i, 0)).append(" — ").append(tableModel.getValueAt(i, 1)).append("\n");
        }

        excelExporter.exportToExcel(tableModel, "output/report.xlsx");
    }

    private void onLoadStopWords() {//загрузка стоп слов
        stopWords = stopWordsLoader.loadStopWords();
        if (stopWords != null && !stopWords.isEmpty()) {
            fileIO.showMessage("Загружено " + stopWords.size() + " стоп-слов.");
        }
    }

    private void onOpenAdvancedSettings() {//открыть расширенные настройки
        settingsDialog.setVisible(true);
    }
}