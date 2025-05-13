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
 *
 * @author Xiang
 */
public class OCRService {
    private final String tessdataPath;

    public OCRService(String tessdataPath) {
        this.tessdataPath = tessdataPath;
    }

    public String doOCR(File image, String lang) throws TesseractException {
        ITesseract ocr = new Tesseract();
        ocr.setDatapath(tessdataPath);
        ocr.setLanguage(lang);
        return ocr.doOCR(image);
    }
}
