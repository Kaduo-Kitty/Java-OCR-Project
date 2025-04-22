/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

/**
 *
 * @author Xiang
 */
public class TestMockOCR {
    public static void main(String[] args) {
        OCRLibrary ocr = new MockOCR(); // 多态用法
        String result = ocr.recognize("test_image.png");
        System.out.println(result);
    }
}
