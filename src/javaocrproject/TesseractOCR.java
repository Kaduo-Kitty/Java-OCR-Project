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
    
    
    @Override
    public String recognize(String imagePath) {
        File imageFile = new File(imagePath);
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng");
        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            return "识别失败：" + e.getMessage();
        }
    }
    
    public static void main(String[] args) {//快速测试
        String img = "C:\\Users\\lovex\\Pictures\\Screenshots";  // 换成真实图片路径
        String out = new TesseractOCR().recognize(img);
        System.out.println("识别结果：\n" + out);
    }
    
}
