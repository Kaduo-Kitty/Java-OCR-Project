package javaocrproject;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;

/**
 * MainUI class is responsible for constructing the Swing graphical user interface of the OCR application.
 * Its functionalities include image drag-and-drop recognition, language and engine settings management,
 * and support for a multilingual user interface.
 * 主界面类，用于构建 OCR 应用的 Swing 图形界面。
 * 功能包括图像拖拽识别、语言与引擎设置、多语言界面支持。
 * 
 * @author Xiang
 */
public class MainUI extends JFrame {
    //UI Components for Settings Tab
    //设置页面中的下拉框组件
    private JComboBox<ComboItem> uiLangCombo;      // User interface language selection combo box
                                                   // 用户界面语言选择框
    private JComboBox<ComboItem> ocrLangCombo;     // OCR recognition language selection combo box
                                                   // OCR 识别语言选择框
    private JComboBox<String> ocrEngineCombo;      // OCR engine selection combo box
                                                   // OCR 引擎选择框
    
    //Corresponding label components for settings.
    //对应的标签组件
    private JLabel uiLangLabel;
    private JLabel ocrLangLabel;
    private JLabel ocrEngineLabel;

    //Main Interface Area Components
    //主界面区域组件
    private JTabbedPane tabbedPane;                // Right-side tabbed pane container (for Output, Settings, History)
                                                   // 右侧标签页容器（用于输出、设置、历史记录）
    private JPanel settingsPanel;                  // Panel for the settings tab page
                                                   // 设置选项卡页面
    private JPanel outputPanel;                    // Panel for the output results tab page
                                                   // 输出结果选项卡页面
    private JPanel previewPanel;                   // Left-side image drag-and-drop and preview area
                                                   // 左侧图像拖拽与预览区
    private JLabel imageLabel;                     // Area for displaying the image
                                                   // 图像显示区域
    private JTextArea outputTextArea;              // Text area to display OCR output text
                                                   // 显示 OCR 输出文本的文本框
    private JTextArea historyTextArea;             // Text area to display OCR history records
                                                   // 显示 OCR 历史记录的文本框
    
    // Application Logic Components
    // 应用程序逻辑组件
    private final Settings settings;               // Global settings object to persist preferences (e.g., languages)
                                                   // 全局设置对象，用于保存偏好设置（如语言）
    private OCRHistoryManager historyManager;      // Manager for OCR history records
                                                   // OCR历史记录管理器
    private final IOcrService ocrService;         // Service interface for performing OCR operations
                                                   // 执行 OCR 操作的服务接口
    
    // Keys for OCR language options, used to retrieve localized names from LanguageManager.
    // OCR 语言选项的键，用于从 LanguageManager 中检索本地化名称。
    private static final String[] OCR_LANGUAGE_KEYS = {"ocrLang.eng", "ocrLang.chi_sim", "ocrLang.chi_tra"};
    
    //Initializes the user interface and sets up application logic components.
    //初始化UI并设定逻辑元件
    public MainUI(Settings settings) {
        this.settings = settings;
        this.historyManager = new OCRHistoryManager(); 
        this.ocrService = new OCRService("tessdata");
        initUI();
    }

    //Initializes the main UI structure and its components.
    //初始化主界面结构与组件。
    private void initUI() {
        setTitle(LanguageManager.get("app.title"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        buildPreviewPanel();
        tabbedPane = new JTabbedPane();
        buildOutputTab();
        buildSettingsTab();
        buildHistoryTab();
        updateUIText();
        updateHistoryDisplay();
        
        add(previewPanel);
        add(tabbedPane);

        setVisible(true);
    }

    //构建左侧图像预览区域，支持图像拖入后识别。
    private void buildPreviewPanel() {
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder(LanguageManager.get("label.preview")));

        imageLabel = new JLabel("", SwingConstants.CENTER);
        previewPanel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        //Builds the left-side image preview area, supporting image drag-in for recognition.
        // 支持用户将图像文件拖入该面板
        new DropTarget(previewPanel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    // Check if files were dropped.
                    // 检查是否有文件被拖放。
                    if (!droppedFiles.isEmpty()) {
                        File imageFile = droppedFiles.get(0);//Get the dropped file；获取拖放文件
                        BufferedImage img = ImageIO.read(imageFile);
                        if (img != null) {
                            //Scale the image proportionally to fit the preview area.
                            // 图像等比缩放以适应预览区域
                            int maxW = previewPanel.getWidth() - 40;
                            int maxH = previewPanel.getHeight() - 60;
                            double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
                            int scaledW = (int) (img.getWidth() * scale);
                            int scaledH = (int) (img.getHeight() * scale);
                            Image scaled = img.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaled));

                            // Perform OCR recognition and output the result to the text area.
                            // OCR 识别并输出结果到文本区
                            outputTextArea.setText(LanguageManager.get("status.processing"));
                            String result = ocrService.doOCR(imageFile, settings.getOcrLang());
                            outputTextArea.setText(result);
                            
                            // Save the OCR recognition record to history.
                            //保存OCR识别记录
                            historyManager.addRecord(result);
                            updateHistoryDisplay();
                        } else {
                            // Show error message for unsupported image format.
                            // 显示不支持的图像格式的错误消息。
                            JOptionPane.showMessageDialog(MainUI.this, LanguageManager.get("error.unsupportedFormat"));
                        }
                    }
                } catch (TesseractException ex) {
                    outputTextArea.setText(LanguageManager.get("error.ocr") + ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    //Builds the output results tab, which displays the OCR recognized text.
    //构建输出结果页，显示 OCR 识别文本。
    private void buildOutputTab() {
        outputPanel = new JPanel(new BorderLayout());
        outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);
        tabbedPane.addTab("", outputPanel);
    }

    //Builds the settings tab, allowing users to select UI language, OCR language, and OCR engine.
    //构建设置页，用户选择界面语言、OCR语言与引擎。
    private void buildSettingsTab() {
        settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        //UI Language Label and Combo Box
        // UI语言标签与选择框
        uiLangLabel = new JLabel();
        settingsPanel.add(uiLangLabel, gbc);
        gbc.gridx = 1;
        uiLangCombo = new JComboBox<>();
        uiLangCombo.addItem(new ComboItem("English", "en"));
        uiLangCombo.addItem(new ComboItem("简体中文", "zh_CN"));
        uiLangCombo.addItem(new ComboItem("繁體中文", "zh_TW"));
        uiLangCombo.setPreferredSize(new Dimension(140, 24));
        uiLangCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                onUILangChanged();
            }
        });
        settingsPanel.add(uiLangCombo, gbc);

        //OCR Language Label and Combo Box
        // OCR语言标签与下拉框
        gbc.gridx = 0;
        gbc.gridy++;
        ocrLangLabel = new JLabel();
        settingsPanel.add(ocrLangLabel, gbc);
        gbc.gridx = 1;
        ocrLangCombo = new JComboBox<>();
        ocrLangCombo.setPreferredSize(new Dimension(140, 24));
        ocrLangCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ComboItem selected = (ComboItem) ocrLangCombo.getSelectedItem();
                if (selected != null) {
                    // Use ComboItem's getValue() to get the corresponding language code.
                    // 使用 ComboItem 的 getValue() 获取对应的语言代码。
                    // Update the OCR language in settings.
                    // 更新设置中的 OCR 语言。
                    settings.setOcrLang(selected.getValue());
                }
            }
        });
        settingsPanel.add(ocrLangCombo, gbc);

        //OCR Engine Label and Combo Box
        // OCR引擎标签与下拉框
        gbc.gridx = 0;
        gbc.gridy++;
        ocrEngineLabel = new JLabel();
        settingsPanel.add(ocrEngineLabel, gbc);
        gbc.gridx = 1;
        ocrEngineCombo = new JComboBox<>();
        ocrEngineCombo.setPreferredSize(new Dimension(140, 24));
        settingsPanel.add(ocrEngineCombo, gbc);
        tabbedPane.addTab("", settingsPanel);
    }

    private void buildHistoryTab() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyTextArea = new JTextArea();
        historyTextArea.setLineWrap(true);
        historyTextArea.setWrapStyleWord(true);
        historyTextArea.setEditable(false);
        historyPanel.add(new JScrollPane(historyTextArea), BorderLayout.CENTER);

        // Button to clear all history records.
        // 一个按钮,清空历史记录
        JButton clearHistoryButton = new JButton(LanguageManager.get("button.clearHistory")); // 我们稍后会在properties文件里添加这个key
        clearHistoryButton.addActionListener(e -> {
            historyManager.clearHistory();
            updateHistoryDisplay();
            JOptionPane.showMessageDialog(MainUI.this, LanguageManager.get("status.historyCleared")); // 提示用户
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearHistoryButton);
        historyPanel.add(buttonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("", historyPanel);
    }
    
    //Refreshes the display of OCR history records in the history text area.
    // 刷新历史记录文本区域的显示
    private void updateHistoryDisplay() {
        StringBuilder sb = new StringBuilder();
        java.util.List<String> records = historyManager.getAllRecords();
        // Check if there are no records.
        // 检查是否没有记录。
        if (records.isEmpty()) {
            sb.append(LanguageManager.get("status.noHistory"));
        } else {
            // Iterate through the list of history records.
            // 遍历历史记录列表。
            for (int i = 0; i < records.size(); i++) {
                sb.append("--- Record ").append(i + 1).append(" ---\n");
                sb.append(records.get(i)).append("\n\n");
            }
        }
        historyTextArea.setText(sb.toString());
        historyTextArea.setCaretPosition(0);
    }
    
    //Refreshes all language-sensitive text content in the user interface.
    //This method is called upon UI language changes and initial loading.
    //刷新界面中所有受语言影响的文本内容。    
    //此方法在 UI 语言更改和初始加载时调用。
    private void updateUIText() {
        setTitle(LanguageManager.get("app.title"));

        uiLangLabel.setText(LanguageManager.get("label.uiLang"));
        ocrLangLabel.setText(LanguageManager.get("label.ocrLang"));
        ocrEngineLabel.setText(LanguageManager.get("label.ocrEngine"));

        tabbedPane.setTitleAt(0, LanguageManager.get("tab.output"));
        tabbedPane.setTitleAt(1, LanguageManager.get("tab.settings"));
        tabbedPane.setTitleAt(2, LanguageManager.get("tab.history"));

        ocrLangCombo.removeAllItems();
        ocrLangCombo.addItem(new ComboItem(LanguageManager.get("ocrLang.eng"), "eng"));
        ocrLangCombo.addItem(new ComboItem(LanguageManager.get("ocrLang.chi_sim"), "chi_sim"));
        ocrLangCombo.addItem(new ComboItem(LanguageManager.get("ocrLang.chi_tra"), "chi_tra"));

        ocrEngineCombo.removeAllItems();
        ocrEngineCombo.addItem("Tesseract");

        if (previewPanel != null && previewPanel.getBorder() instanceof javax.swing.border.TitledBorder) {
            ((javax.swing.border.TitledBorder) previewPanel.getBorder()).setTitle(LanguageManager.get("label.preview"));
            previewPanel.repaint();
        
        String currentOcrLang = settings.getOcrLang();
        for (int i = 0; i < ocrLangCombo.getItemCount(); i++) {
            ComboItem item = ocrLangCombo.getItemAt(i);
            if (item.getValue().equals(currentOcrLang)) {
                ocrLangCombo.setSelectedItem(item);
                break;
            }
        }
        }
    }

    //Handles the UI language dropdown selection change event.
    //Updates the system locale and refreshes the entire UI text.
    //语言下拉框变更事件，更新系统语言并刷新界面。
    private void onUILangChanged() {
        ComboItem selected = (ComboItem) uiLangCombo.getSelectedItem();
        if (selected == null) return;
        String langCode = selected.getValue();
        Locale locale;
        locale = switch (langCode) {
            case "zh_CN" -> Locale.SIMPLIFIED_CHINESE;
            case "zh_TW" -> Locale.TRADITIONAL_CHINESE;
            default -> Locale.ENGLISH;
        };
        settings.setUiLocale(langCode);
        LanguageManager.setLocale(locale);
        updateUIText();
    }
    //A generic inner class for encapsulating display name and actual value in JComboBox items.
    //通用下拉框封装类（显示名称 + 值）
    static class ComboItem {
        private final String label;
        private final String value;

        public ComboItem(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String toString() {
            return label;
        }

        public String getValue() {
            return value;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ComboItem item = (ComboItem) obj;
            return value != null ? value.equals(item.value) : item.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}