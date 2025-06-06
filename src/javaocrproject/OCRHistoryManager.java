/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiang
 */
/**
 * OCRHistoryManager 类负责管理 OCR 识别的文本历史记录。
 * 它提供添加、获取和保存历史记录的功能。
 */
public class OCRHistoryManager {

    // 用于存储 OCR 历史记录的列表
    private final List<String> ocrHistory;

    /**
     * 构造方法，初始化 OCRHistoryManager。
     */
    public OCRHistoryManager() {
        this.ocrHistory = new ArrayList<>();
    }

    /**
     * 添加新的 OCR 识别结果到历史记录中。
     * @param text 识别到的文本内容。
     */
    public void addRecord(String text) {
        if (text != null && !text.trim().isEmpty()) {
            this.ocrHistory.add(text);
        }
    }

    /**
     * 获取所有 OCR 历史记录。
     * @return 包含所有历史记录的列表。
     */
    public List<String> getAllRecords() {
        return new ArrayList<>(ocrHistory); // 返回一个副本，防止外部直接修改
    }

    //清空所有历史记录。
    public void clearHistory() {
        this.ocrHistory.clear();
    }
}
