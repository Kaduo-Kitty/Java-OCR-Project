/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject_old;

/**
 *
 * @author Xiang
 */
public class MockOCR implements OCRLibrary{
    @Override
    public String recognize(String imagePath) {
        return "（模拟识别）您上传的图片路径是：" + imagePath + "\n识别结果：这是一个测试文本。";
    }
}
