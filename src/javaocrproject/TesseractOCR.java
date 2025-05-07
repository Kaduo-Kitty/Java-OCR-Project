/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import net.sourceforge.tess4j.*;
import java.io.File;

/**
 *
 * @author Xiang
 */
public class TesseractOCR implements OCRLibrary {
    public String recognize(String imagePath) {
        File imageFile = new File(imagePath);
        ITesseract tesseract = new Tesseract();

        // 设置语言包路径（可按实际修改）
        tesseract.setDatapath("tessdata"); // tessdata 文件夹放在项目根目录
        tesseract.setLanguage("eng");

        try {
            return tesseract.doOCR(imageFile);
        } 
        catch (TesseractException e) {
            return "识别失败：" + e.getMessage();
        }
    }
}
