/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import java.util.ArrayList;
import java.util.List;

/**
 * OCRHistoryManager class is responsible for managing the text history of OCR (Optical Character Recognition) results.
 * It provides functionalities to add, retrieve, and clear historical records.
 * OCRHistoryManager 类负责管理 OCR（光学字符识别）结果的文本历史记录。
 * 它提供添加、获取和清空历史记录的功能。
 *
 * @author Xiang
 */
public class OCRHistoryManager {
    // A private list to store OCR history records.
    // 用于存储 OCR 历史记录的私有列表
    private final List<String> ocrHistory;

    //Constructor for OCRHistoryManager.
    //OCRHistoryManager 的构造方法。
    public OCRHistoryManager() {
        this.ocrHistory = new ArrayList<>();
    }

    //Adds a new OCR recognition result to the history.
    //Only non-null and non-empty texts are added.
    //将新的 OCR 识别结果添加到历史记录中。
    //只添加非空且非空白的文本。
    public void addRecord(String text) {
        if (text != null && !text.trim().isEmpty()) {
            this.ocrHistory.add(text);
        }
    }

    //Retrieves all stored OCR history records.
    //获取所有 OCR 历史记录。
    public List<String> getAllRecords() {
        return new ArrayList<>(ocrHistory);
    }

    //Clears all OCR history records from the list.
    //清空所有历史记录。
    public void clearHistory() {
        this.ocrHistory.clear();
    }
}
