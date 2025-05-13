/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Xiang
 */
public class LanguageManager {
    private ResourceBundle bundle;

    public LanguageManager(String localeCode) {
        switchLocale(localeCode);
    }

    public void switchLocale(String localeCode) {
        Locale locale = "zh".equals(localeCode)
            ? Locale.SIMPLIFIED_CHINESE
            : Locale.ENGLISH;
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public String get(String key) {
        return bundle.getString(key);
    }
}
