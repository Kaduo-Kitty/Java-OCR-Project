package javaocrproject;

public class MainApp {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Settings settings = new Settings();
            new MainUI(settings);
        });
    }
}