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
    private boolean removeStopWords = true; //flag
    private JCheckBox removeStopWordsCheckbox;

    public AdvancedSettingsDialog(JFrame parent) {// управление расширенными настройками
        super(parent, "Расширенные настройки", true); 
        setSize(350, 150);
        setLocationRelativeTo(parent);

       
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       
        removeStopWordsCheckbox = new JCheckBox("Убирать стоп-слова из текста");
        removeStopWordsCheckbox.setSelected(removeStopWords);
        panel.add(removeStopWordsCheckbox);

        
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            removeStopWords = removeStopWordsCheckbox.isSelected();
            dispose(); 
        });

       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));//контейнер для размещения кнопки сохранения
        buttonPanel.add(saveButton);

       
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean showSettingsDialog() {
        setVisible(true); //не позволяет взаимодействовать с главным окном
        return removeStopWords;
    }

   
    public boolean shouldRemoveStopWords() {
        return removeStopWords;//проверка на флаг
    }
}
