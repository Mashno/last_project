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

public class AdvancedSettingsDialog extends JDialog {
    private boolean removeStopWords = true; // значение по умолчанию
    private JCheckBox removeStopWordsCheckbox;

    public AdvancedSettingsDialog(JFrame parent) {
        super(parent, "Расширенные настройки", true); // модальное окно
        setSize(350, 150);
        setLocationRelativeTo(parent);

        // Панель с настройками
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Чекбокс: убирать стоп-слова
        removeStopWordsCheckbox = new JCheckBox("Убирать стоп-слова из текста");
        removeStopWordsCheckbox.setSelected(removeStopWords);
        panel.add(removeStopWordsCheckbox);

        // Кнопка "Сохранить"
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            removeStopWords = removeStopWordsCheckbox.isSelected();
            dispose(); // закрыть окно
        });

        // Панель с кнопкой
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);

        // Добавляем всё на форму
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Показывает диалог и возвращает настройки
     */
    public boolean showSettingsDialog() {
        setVisible(true); // блокирующий вызов (модальное окно)
        return removeStopWords;
    }

    /**
     * Возвращает текущее значение настройки
     */
    public boolean shouldRemoveStopWords() {
        return removeStopWords;
    }
}
