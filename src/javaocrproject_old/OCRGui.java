/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject_old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 *
 * @author Xiang
 */
public class OCRGui extends JFrame {
    private JTextField imagePathField;
    private JTextArea resultArea;
    private JLabel imagePreviewLabel;
    
    public OCRGui() {
        setTitle("Java OCR 系统");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中窗口

        // 设置主面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 顶部：图片路径 + 选择按钮 + 图片预览
        JPanel topPanel = new JPanel(new BorderLayout());
        imagePreviewLabel = new JLabel("图片预览", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        imagePreviewLabel.setBorder(BorderFactory.createEtchedBorder());
        imagePathField = new JTextField();
        imagePathField.setEditable(false);
        JButton chooseButton = new JButton("选择图片");
        chooseButton.addActionListener(e -> chooseImage());
        topPanel.add(imagePathField, BorderLayout.CENTER);
        topPanel.add(chooseButton, BorderLayout.EAST);

        // 中部：识别结果区域
        resultArea = new JTextArea();
        resultArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 底部：识别按钮
        JButton recognizeButton = new JButton("识别");
        recognizeButton.addActionListener(e -> recognizeImage());

        // 布局
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(imagePreviewLabel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(recognizeButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePathField.setText(selectedFile.getAbsolutePath());

            // 显示图片预览
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            imagePreviewLabel.setText(""); // 移除文字
        }
    }

    private void recognizeImage() {
        String imagePath = imagePathField.getText();
        if(imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先选择图片！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 暂时使用 MockOCR 进行测试
        OCRLibrary ocr = new MockOCR();
        String result = OCRManager.recognize(imagePath, "tesseract");
        resultArea.setText(result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OCRGui gui = new OCRGui();
            gui.setVisible(true);
        });
    }
}
