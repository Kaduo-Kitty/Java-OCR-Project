/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
/**
 *
 * @author Xiang
 *  
 * 本类负责搭建整个 Swing 界面，并处理用户交互事件。
 * 界面分为左右两部分：
 *   - 左侧预览区：用户拖拽图片到此，显示缩略并自动执行 OCR
 *   - 右侧选项区：包含 "设置" 和 "输出" 两个标签页
 *     - "设置" 页：切换界面语言、OCR 语言、OCR 引擎
 *     - "输出" 页：显示 OCR 结果，并提供导出为 TXT 的功能
 * 
 * 业务逻辑通过以下几个类拆分：
 *   Settings       - 存储用户偏好（uiLocale, ocrLang, ocrEngine）
 *   LanguageManager- 管理国际化资源，根据 locale 加载 i18n.messages
 *   OCRService     - 封装 Tess4J 调用，执行 OCR
 *   MainUI         - 负责界面及事件，委托以上类执行具体操作
 * 
 */
public class MainUI extends JFrame {
    private final Settings settings;
    private final LanguageManager langMgr;
    private final OCRService ocrService;
    
    // ------------------- Swing 组件 -------------------
    private JPanel previewPanel;             // 左侧拖拽/预览面板
    private JLabel previewLabel;             // 预览面板中显示图片或提示文本
    private JLabel lblUILang;                // 设置页：界面语言标签
    private JComboBox<String> uiLangCombo;   // 设置页：界面语言下拉框
    private JLabel lblOCRLang;               // 设置页：OCR 语言标签
    private JComboBox<String> ocrLangCombo;  // 设置页：OCR 语言下拉框
    private JLabel lblOCREngine;             // 设置页：OCR 引擎标签
    private JComboBox<String> engineCombo;   // 设置页：OCR 引擎下拉框
    private JTabbedPane tabPane;             // 右侧标签页容器
    private JTextArea outputArea;            // 输出页：显示 OCR 文本
    private JButton exportBtn;               // 输出页：导出按钮

    public MainUI() {//UI布局
        settings = new Settings();
        langMgr = new LanguageManager(settings.getUiLocale());
        ocrService = new OCRService("tessdata");

        initFrame();
        initPreviewPanel();
        initTabbedPane();
        initDragAndDrop();

        setVisible(true);
    }

    private void initFrame() {
        setTitle(langMgr.get("app.title"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initPreviewPanel() {
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(new TitledBorder(langMgr.get("label.preview")));
        previewLabel = new JLabel(langMgr.get("label.dragHere"), SwingConstants.CENTER);
        previewPanel.add(previewLabel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.WEST);
    }

    private void initTabbedPane() {
        tabPane = new JTabbedPane();
        tabPane.addTab(langMgr.get("tab.settings"), buildSettingsTab());
        tabPane.addTab(langMgr.get("tab.output"), buildOutputTab());
        add(tabPane, BorderLayout.CENTER);
    }

    private JPanel buildSettingsTab() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        // UI 语言
        gbc.gridx = 0; gbc.gridy = 0;
        lblUILang = new JLabel(langMgr.get("label.uiLang"));
        p.add(lblUILang, gbc);
        gbc.gridx = 1;
        uiLangCombo = new JComboBox<>(new String[]{"简体中文","English"});
        uiLangCombo.setSelectedItem(settings.getUiLocale().equals("zh") ? "简体中文" : "English");
        uiLangCombo.addItemListener(this::onUILangChanged);
        p.add(uiLangCombo, gbc);

        // OCR 语言
        gbc.gridx = 0; gbc.gridy++;
        lblOCRLang = new JLabel(langMgr.get("label.ocrLang"));
        p.add(lblOCRLang, gbc);
        gbc.gridx = 1;
        ocrLangCombo = new JComboBox<>(new String[]{"eng","chi_sim"});
        ocrLangCombo.setSelectedItem(settings.getOcrLang());
        ocrLangCombo.addItemListener(e -> {
            if (e.getStateChange()==ItemEvent.SELECTED)
                settings.setOcrLang((String)ocrLangCombo.getSelectedItem());
        });
        p.add(ocrLangCombo, gbc);

        // OCR 引擎
        gbc.gridx = 0; gbc.gridy++;
        lblOCREngine = new JLabel(langMgr.get("label.ocrEngine"));
        p.add(lblOCREngine, gbc);
        gbc.gridx = 1;
        engineCombo = new JComboBox<>(new String[]{"Tesseract"});
        engineCombo.setSelectedItem(settings.getOcrEngine());
        engineCombo.addItemListener(e -> {
            if (e.getStateChange()==ItemEvent.SELECTED)
                settings.setOcrEngine((String)engineCombo.getSelectedItem());
        });
        p.add(engineCombo, gbc);

        return p;
    }

    private JPanel buildOutputTab() {
        JPanel p = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        p.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        exportBtn = new JButton(langMgr.get("button.exportTxt"));
        exportBtn.addActionListener(this::onExport);
        p.add(exportBtn, BorderLayout.SOUTH);
        return p;
    }

    private void initDragAndDrop() {
        new DropTarget(previewPanel, DnDConstants.ACTION_COPY,
            new DropTargetAdapter() {
            @SuppressWarnings("unchecked")
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> files = (List<File>)dtde.getTransferable().getTransferData(
                            DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        File img = files.get(0);
                        showPreview(img);
                        startOCR(img);
                    }
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        }, true);
    }

    private void showPreview(File imgFile) throws IOException {
        BufferedImage img = ImageIO.read(imgFile);
        int w = previewPanel.getWidth() - 20;
        int h = (int)(img.getHeight() * (w / (double)img.getWidth()));
        ImageIcon icon = new ImageIcon(img.getScaledInstance(w,h,Image.SCALE_SMOOTH));
        previewLabel.setText("");
        previewLabel.setIcon(icon);
    }

    private void startOCR(File imgFile) {
        outputArea.setText(langMgr.get("status.processing"));
        new Thread(() -> {
            try {
                String text = ocrService.doOCR(imgFile, settings.getOcrLang());
                SwingUtilities.invokeLater(() -> {
                    outputArea.setText(text);
                    tabPane.setSelectedIndex(1);
                });
            } catch (TesseractException ex) {
                showError(ex);
            }
        }).start();
    }

    private void onUILangChanged(ItemEvent e) {//界面语言，下拉选项
        if (e.getStateChange()!=ItemEvent.SELECTED) return;
        String sel = (String)uiLangCombo.getSelectedItem();
        settings.setUiLocale("简体中文".equals(sel) ? "zh" : "en");
        langMgr.switchLocale(settings.getUiLocale());
        updateUIText();
    }

    private void onExport(ActionEvent ev) {//以TXT文件格式，导出OCR文本
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(langMgr.get("fileChooser.saveTitle"));
        chooser.setSelectedFile(new File("ocr_result.txt"));
        if (chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try (Writer w = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                w.write(outputArea.getText());
            } catch (IOException ex) {
                showError(ex);
            }
        }
    }

    private void updateUIText() {//界面语言切换
        setTitle(langMgr.get("app.title"));
        ((TitledBorder)previewPanel.getBorder()).setTitle(langMgr.get("label.preview"));
        previewLabel.setText(langMgr.get("label.dragHere"));
        tabPane.setTitleAt(0, langMgr.get("tab.settings"));
        tabPane.setTitleAt(1, langMgr.get("tab.output"));
        lblUILang.setText(langMgr.get("label.uiLang"));
        lblOCRLang.setText(langMgr.get("label.ocrLang"));
        lblOCREngine.setText(langMgr.get("label.ocrEngine"));
        exportBtn.setText(langMgr.get("button.exportTxt"));
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void showError(Exception ex) {//错误提示
        ex.printStackTrace();
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE));
    }

    public static void main(String[] args) {//主函数，启动SwingUI
        SwingUtilities.invokeLater(MainUI::new);
    }
}