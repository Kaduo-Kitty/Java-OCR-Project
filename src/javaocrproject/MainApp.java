package javaocrproject;

/**
 * MainApp class serves as the entry point for the Local OCR System application.
 * It initializes the application settings and launches the main user interface on the Event Dispatch Thread (EDT).
 *
 * MainApp 类作为本地 OCR 系统应用程序的入口点。
 * 它初始化应用程序设置并在事件调度线程 (EDT) 上启动主用户界面。
 * * 
 * @author Xiang
 */
public class MainApp {
    public static void main(String[] args) {
        // creating and showing this application's GUI.
        // 创建并显示此应用程序的 GUI。
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Create an instance of Settings to manage application preferences.
            // 创建一个 Settings 实例来管理应用程序偏好设置。
            Settings settings = new Settings();
            // Create and display the main user interface, passing the settings object.
            // 创建并显示主用户界面，传入 settings 对象。
            new MainUI(settings);
        });
    }
}