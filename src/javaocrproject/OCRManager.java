/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

/**
 *
 * @author Xiang
 */
public class OCRManager {
     public static String recognize(String imagePath, String engineName) {
        OCRLibrary ocr;

        switch (engineName.toLowerCase()) {
            case "mock":
                ocr = new MockOCR();
                break;

            case "tesseract":
                ocr = new TesseractOCR();
                break;

            default:
                return "错误：未知识别引擎 \"" + engineName + "\"";
        }

        return ocr.recognize(imagePath);
    }
}
