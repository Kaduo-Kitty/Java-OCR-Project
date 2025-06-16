/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

/**
 * Settings class manages and stores the user preferences and application settings.
 * It holds configurations such as the selected UI locale, OCR language, and OCR engine.
 * Settings 类管理并存储用户偏好和应用程序设置。
 * 它保存了例如选定的 UI 区域设置、OCR 语言和 OCR 引擎等配置。
 * 
 * @author Xiang
 */
public class Settings {
    private String uiLocale = "en";        // "en" 或 "zh"
    private String ocrLang = "eng";         // "eng", "chi_sim", ...
    private String ocrEngine = "Tesseract"; // "Tesseract", "PaddleOCR", ...

    //Retrieves the currently selected UI locale.
    //获取当前选定的 UI 区域设置。
    public String getUiLocale() {
        return uiLocale;
    }

    //Sets the UI locale for the application.
    //设置应用程序的 UI 区域设置。
    public void setUiLocale(String uiLocale) {
        this.uiLocale = uiLocale;
    }
    
    //Retrieves the currently selected OCR language.
    //获取当前选定的 OCR 语言。
    public String getOcrLang() {
        return ocrLang;
    }

    //Sets the OCR language for the application.
    //设置应用程序的 OCR 语言。
    public void setOcrLang(String ocrLang) {
        this.ocrLang = ocrLang;
    }

    //Retrieves the currently selected OCR engine.
    //获取当前选定的 OCR 引擎。
    public String getOcrEngine() {
        return ocrEngine;
    }
    
    //Sets the OCR engine to be used by the application.
    //设置应用程序要使用的 OCR 引擎。
    public void setOcrEngine(String ocrEngine) {
        this.ocrEngine = ocrEngine;
    }
}
