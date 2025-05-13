/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

/**
 *
 * @author Xiang
 */
public class Settings {
    private String uiLocale = "en";        // "en" 或 "zh"
    private String ocrLang = "eng";         // "eng", "chi_sim", ...
    private String ocrEngine = "Tesseract"; // "Tesseract", "PaddleOCR", ...

    public String getUiLocale() {
        return uiLocale;
    }

    public void setUiLocale(String uiLocale) {
        this.uiLocale = uiLocale;
    }

    public String getOcrLang() {
        return ocrLang;
    }

    public void setOcrLang(String ocrLang) {
        this.ocrLang = ocrLang;
    }

    public String getOcrEngine() {
        return ocrEngine;
    }

    public void setOcrEngine(String ocrEngine) {
        this.ocrEngine = ocrEngine;
    }
}
