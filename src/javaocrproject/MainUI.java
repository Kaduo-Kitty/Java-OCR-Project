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
 * 主界面类，用于构建 OCR 应用的 Swing 图形界面。
 * 功能包括图像拖拽识别、语言与引擎设置、多语言界面支持。
 */
public class MainUI extends JFrame {

    // 设置页面中的下拉框组件
    private JComboBox<ComboItem> uiLangCombo;      // 用户界面语言选择框
    private JComboBox<ComboItem> ocrLangCombo;        // OCR 识别语言选择框
    private JComboBox<String> ocrEngineCombo;      // OCR 引擎选择框

    // 对应的标签组件
    private JLabel uiLangLabel;
    private JLabel ocrLangLabel;
    private JLabel ocrEngineLabel;

    // 主界面区域组件
    private JTabbedPane tabbedPane;                // 右侧标签页容器
    private JPanel settingsPanel;                  // 设置选项卡页面
    private JPanel outputPanel;                    // 输出结果选项卡页面
    private JPanel previewPanel;                   // 左侧图像拖拽与预览区
    private JLabel imageLabel;                     // 图像显示区域
    private JTextArea outputTextArea;              // 显示 OCR 输出文本的文本框
    private JTextArea historyTextArea;             // 显示 OCR 历史记录的文本框
    
    private Settings settings;                     // 全局设置对象
    private OCRHistoryManager historyManager;      //OCR历史记录管理器
    /**
     * 构造方法，初始化 UI。
     * @param settings 设置数据对象（保存语言等偏好）
     */
    public MainUI(Settings settings) {
        this.settings = settings;
        this.historyManager = new OCRHistoryManager();
        initUI();
    }

    //初始化主界面结构与组件。
    private void initUI() {
        setTitle(LanguageManager.get("app.title"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2)); // 左右平分两栏

        buildPreviewPanel();             // 图像拖拽与预览
        tabbedPane = new JTabbedPane();  // 设置与输出标签页
        buildOutputTab();
        buildSettingsTab();
        buildHistoryTab();              //构建历史记录标签页
        updateUIText();                 // 加载初始语言文字
        updateHistoryDisplay();         // 新增：初始化历史记录显示
        
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

        // 支持用户将图像文件拖入该面板
        new DropTarget(previewPanel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (!droppedFiles.isEmpty()) {
                        File imageFile = droppedFiles.get(0);
                        BufferedImage img = ImageIO.read(imageFile);
                        if (img != null) {
                            // 图像等比缩放以适应预览区域
                            int maxW = previewPanel.getWidth() - 40;
                            int maxH = previewPanel.getHeight() - 60;
                            double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
                            int scaledW = (int) (img.getWidth() * scale);
                            int scaledH = (int) (img.getHeight() * scale);
                            Image scaled = img.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaled));

                            // OCR 识别并输出结果到文本区
                            Tesseract tesseract = new Tesseract();
                            tesseract.setDatapath("tessdata");
                            tesseract.setLanguage(settings.getOcrLang()); // 识别OCR语言
                            String result = tesseract.doOCR(imageFile);
                            outputTextArea.setText(result);
                            tabbedPane.setSelectedIndex(0); // 自动跳转到输出页
                            historyManager.addRecord(result);// 将 OCR 结果添加到历史记录管理器中
                            updateHistoryDisplay();//OCR 后刷新历史记录显示
                        } else {
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

    //构建设置页，用户选择界面语言、OCR语言与引擎。
    private void buildSettingsTab() {
        settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

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
                // 获取 ComboItem 对象
                ComboItem selected = (ComboItem) ocrLangCombo.getSelectedItem();
                if (selected != null) {
                    // 使用 ComboItem 的 getValue() 获取 Tesseract 对应的语言代码
                    settings.setOcrLang(selected.getValue());
                    // System.out.println("OCR Language changed to: " + settings.getOcrLang()); // 调试信息
                }
            }
        });
        settingsPanel.add(ocrLangCombo, gbc);

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
        historyTextArea.setEditable(false); // 历史记录不应该被编辑
        historyPanel.add(new JScrollPane(historyTextArea), BorderLayout.CENTER);

        // 一个按钮,清空历史记录
        JButton clearHistoryButton = new JButton(LanguageManager.get("button.clearHistory")); // 我们稍后会在properties文件里添加这个key
        clearHistoryButton.addActionListener(e -> {
            historyManager.clearHistory(); // 调用历史管理器清空记录
            updateHistoryDisplay(); // 刷新显示
            JOptionPane.showMessageDialog(MainUI.this, LanguageManager.get("status.historyCleared")); // 提示用户
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearHistoryButton);
        historyPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("", historyPanel); // 暂时设置空标题，将在updateUIText中更新
    }
    
    // 刷新历史记录文本区域的显示
    private void updateHistoryDisplay() {
        StringBuilder sb = new StringBuilder();
        java.util.List<String> records = historyManager.getAllRecords(); // 获取所有历史记录 
        if (records.isEmpty()) {
            sb.append(LanguageManager.get("status.noHistory")); // 稍后在properties文件里添加这个key
        } else {
            for (int i = 0; i < records.size(); i++) { // 遍历历史记录列表 
                sb.append("--- Record ").append(i + 1).append(" ---\n");
                sb.append(records.get(i)).append("\n\n");
            }
        }
        historyTextArea.setText(sb.toString());
        historyTextArea.setCaretPosition(0); // 滚动到顶部
    }
    
    //刷新界面中所有受语言影响的文本内容。
    private void updateUIText() {
        setTitle(LanguageManager.get("app.title"));

        uiLangLabel.setText(LanguageManager.get("label.uiLang"));
        ocrLangLabel.setText(LanguageManager.get("label.ocrLang"));
        ocrEngineLabel.setText(LanguageManager.get("label.ocrEngine"));

        tabbedPane.setTitleAt(0, LanguageManager.get("tab.output"));
        tabbedPane.setTitleAt(1, LanguageManager.get("tab.settings"));
        tabbedPane.setTitleAt(2, LanguageManager.get("tab.history")); // 历史记录标签页标题

        ocrLangCombo.removeAllItems();
        ocrLangCombo.addItem(new ComboItem(LanguageManager.get("ocrLang.eng"), "eng"));
        ocrLangCombo.addItem(new ComboItem(LanguageManager.get("ocrLang.chi_sim"), "chi_sim"));
        ocrLangCombo.addItem(new ComboItem(LanguageManager.get("ocrLang.chi_tra"), "chi_tra"));

        ocrEngineCombo.removeAllItems();
        ocrEngineCombo.addItem("Tesseract");

        if (previewPanel != null && previewPanel.getBorder() instanceof javax.swing.border.TitledBorder) {
            ((javax.swing.border.TitledBorder) previewPanel.getBorder()).setTitle(LanguageManager.get("label.preview"));
            previewPanel.repaint();
            
        // ***** 新增：确保 OCR 语言下拉框选中当前设置的语言 *****
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

    //语言下拉框变更事件，更新系统语言并刷新界面。
    private void onUILangChanged() {
        ComboItem selected = (ComboItem) uiLangCombo.getSelectedItem();
        if (selected == null) return;
        String langCode = selected.getValue();
        Locale locale;
        switch (langCode) {
            case "zh_CN": locale = Locale.SIMPLIFIED_CHINESE; break;
            case "zh_TW": locale = Locale.TRADITIONAL_CHINESE; break;
            default: locale = Locale.ENGLISH;
        }
        settings.setUiLocale(langCode);
        LanguageManager.setLocale(locale);
        updateUIText();
    }

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