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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.table.*;

public class ExcelExporter {
    private final JFrame parentFrame;

    public ExcelExporter(JFrame parent) {
        this.parentFrame = parent;
    }

    /**
     * Экспортирует данные таблицы в Excel (.xlsx)
     */
    public void exportToExcel(DefaultTableModel tableModel, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel файлы (.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File(defaultFileName));

        if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            if (!filePath.toString().endsWith(".xlsx")) {
                filePath = Paths.get(filePath.toString() + ".xlsx");
            }
            writeExcel(tableModel, filePath);
        }
    }

    /**
     * Записывает данные из таблицы в Excel файл
     */
    private void writeExcel(DefaultTableModel tableModel, Path filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Частота слов");

            // Записываем заголовки
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(tableModel.getColumnName(i));
            }

            // Записываем данные
            for (int rowIdx = 0; rowIdx < tableModel.getRowCount(); rowIdx++) {
                Row row = sheet.createRow(rowIdx + 1);
                for (int colIdx = 0; colIdx < tableModel.getColumnCount(); colIdx++) {
                    Object value = tableModel.getValueAt(rowIdx, colIdx);
                    Cell cell = row.createCell(colIdx);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // Автоподгон ширины столбцов
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Сохраняем файл
            try (OutputStream out = new FileOutputStream(filePath.toFile())) {
                workbook.write(out);
            }

            JOptionPane.showMessageDialog(parentFrame,
                    "Отчёт успешно сохранён в Excel:\n" + filePath,
                    "Успех", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Ошибка при сохранении Excel-файла:\n" + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
