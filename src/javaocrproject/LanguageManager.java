package javaocrproject;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LanguageManager class is responsible for handling multilingual resource loading and switching.
 * It manages i18n/messages_*.properties files through ResourceBundle.
 * LanguageManager 类负责处理多语言资源加载与切换。
 * 它通过 ResourceBundle 管理 i18n/messages_*.properties 文件。
 * 
 * @author Xiang
 */
public class LanguageManager {

    // Default language resource: English (used for initialization).
    // The ResourceBundle loads localized messages from property files (e.g., messages_en.properties).
    // 默认语言资源：英文（用于初始化）。
    // ResourceBundle 从属性文件（例如 messages_en.properties）加载本地化消息。
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.ENGLISH);
    
    
    //Sets the current UI locale and reloads the corresponding language resource bundle.
    //This method allows the application's language to be changed dynamically.
    //设置当前界面语言区域，并重新加载对应的语言资源包。
    //@param locale 目标语言区域（如 Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE 等）
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    //Retrieves the localized text corresponding to a given key from the current language resource.
    //根据键从当前语言资源中获取对应的本地化文本。
    public static String get(String key) {
        return bundle.getString(key);
    }
}
