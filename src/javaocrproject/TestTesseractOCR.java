package javaocrproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Xiang
 */
public class TestTesseractOCR {
    public static void main(String[] args) {
        // 请改成你本地的一个图片完整路径（包含文件名和扩展名）
        String img = "\"C:\\Users\\lovex\\OneDrive - Sunway Education Group\\Sunway JB\\Semester 3\\Java Programming\\Assignment\\Java OCR Project\\JavaOCRProject\\image\\Screenshot 2025-03-24 085233.png\"";
        String result = new TesseractOCR().recognize(img);
        System.out.println("OCR 识别结果：\n" + result);
    }
}
