/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import java.io.File;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * * OCRService class is a concrete implementation of the IOcrService interface.
 * It extends BaseService and uses the Tess4J library to perform Optical Character Recognition (OCR).
 * OCRService 类是 IOcrService 接口的具体实现。
 * 它继承 BaseService 并使用 Tess4J 库执行光学字符识别（OCR）。
 * 
 * @author Xiang
 */
public class OCRService extends BaseService implements IOcrService {
    private final String tessdataPath;

    //Constructor for OCRService.
    //OCRService 的构造方法。
    public OCRService(String tessdataPath) {
        super("TesseractEngine");
        this.tessdataPath = tessdataPath;
    }

    //Performs OCR on a given image file using the specified language.
    //This method creates a Tesseract instance, sets the data path and language, then executes the OCR.
    //使用指定语言对给定的图像文件执行 OCR。
    //此方法创建一个 Tesseract 实例，设置数据路径和语言，然后执行 OCR。
    @Override
    public String doOCR(File image, String lang) throws TesseractException {
        ITesseract ocr = new Tesseract();
        ocr.setDatapath(tessdataPath);
        ocr.setLanguage(lang);
        return ocr.doOCR(image);
    }
    
    //Performs OCR on a given image file using the specified language and DPI setting.
    //This overloaded method provides an option to specify the image's DPI, which can improve recognition accuracy.
    //使用指定语言和 DPI 设置对给定的图像文件执行 OCR。
    //此重载方法提供了指定图像 DPI 的选项，这可以提高识别准确性。
    @Override
    public String doOCR(File image, String lang, int dpi) throws TesseractException {
        ITesseract ocr = new Tesseract();
        ocr.setDatapath(tessdataPath);
        ocr.setLanguage(lang);
        ocr.setVariable("user_defined_dpi", String.valueOf(dpi)); 
        return ocr.doOCR(image);
    }
}
