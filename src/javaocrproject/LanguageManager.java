package javaocrproject;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LanguageManager 类负责处理多语言资源加载与切换。
 * 它通过 ResourceBundle 管理 i18n/messages_*.properties 文件。
 */
public class LanguageManager {

    // 默认语言资源：英文（用于初始化）
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.ENGLISH);

    /**
     * 设置当前界面语言区域，并重新加载对应的语言资源包。
     * @param locale 目标语言区域（如 Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE 等）
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    /**
     * 根据键从当前语言资源中获取对应的本地化文本。
     * @param key 属性键（如 "label.uiLang"）
     * @return 对应语言的字符串值
     */
    public static String get(String key) {
        return bundle.getString(key);
    }
}
