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
    private boolean removeStopWords = true; 
    private String selectedLanguage = "RU";
     
    private JComboBox<String> languageComboBox;
    private JCheckBox removeStopWordsCheckbox;

    public AdvancedSettingsDialog(JFrame parent) {
        super(parent, "Расширенные настройки", true); 
        setSize(350, 150);
        setLocationRelativeTo(parent);

       
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       
        removeStopWordsCheckbox = new JCheckBox("Убирать стоп-слова из текста");
        removeStopWordsCheckbox.setSelected(removeStopWords);
        panel.add(removeStopWordsCheckbox);

        languageComboBox = new JComboBox<>(new String[]{"RU", "EN"});
        languageComboBox.setSelectedItem(selectedLanguage);
        panel.add(new JLabel("Выберите язык текста:"));
        panel.add(languageComboBox);
        
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            removeStopWords = removeStopWordsCheckbox.isSelected();
            selectedLanguage = (String) languageComboBox.getSelectedItem();
            dispose(); 
        });

       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);

       
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean showSettingsDialog() {
        setVisible(true); // блокирующий вызов (модальное окно)
        return removeStopWords;
    }

   
    public boolean shouldRemoveStopWords() {
        return removeStopWords;
    }
    
    public String getSelectedLanguage() {
        return selectedLanguage;
    }
}
